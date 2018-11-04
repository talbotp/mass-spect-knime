package msk.deprecated.node.read.imzML.loop.col;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.LoopStartNodeTerminator;

import imzML.ImzML;
import msk.node.read.imzML.loop.rowSpec.ImzMLChunkReaderRowSpectrumNodeModel;
import msk.util.ImzMLUtil;
import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;

/**
 * This is the model implementation of ImzMLChunkReaderColSpectrum. This node is
 * used for reading in imzML datasets in chunks of spectra. We note that we
 * store the spectrums in the columns of the output ports.
 *
 * @author Andrew P Talbot
 * @version 30/06/2018
 */
public class ImzMLChunkReaderColSpectrumNodeModel extends NodeModel implements LoopStartNodeTerminator {

	// Init a logger
	NodeLogger LOGGER = NodeLogger.getLogger(ImzMLChunkReaderRowSpectrumNodeModel.class);

	// Create the ID for the file chooser.
	protected static final String FILE_CHOOSER_ID = "file_chooser_id_imzML";

	// Here, we create the settingsModel for the choice in the dialog.
	// Not caps lock for readability later on.
	private final SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID, null);

	// Here are the fields used in our Model.
	private boolean isFirstPass = true;
	private boolean isTerminated = false;

	// ImzML fields
	private ImzML imzML;
	private int currentXPixel;
	private int currentYPixel;
	private int totalXPixels;
	private int totalYPixels;

	private int currentIteration;
	private int nIterations;
	private int nRows;

	// DataTable fields
	private DataType[] colSpec;

	/**
	 * Constructor for the node model.
	 */
	protected ImzMLChunkReaderColSpectrumNodeModel() {
		/**
		 * Output port 0 is the mz Array(s). Output port 1 is the intensity
		 * Arrays.
		 */
		super(0, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		// Set everything once, cannot set the spec though as it may need to
		// change.
		if (isFirstPass) {
			String filename = m_file_chooser.getStringValue();
			imzML = ImzMLUtil.getImzML(filename);

			totalXPixels = imzML.getWidth();
			totalYPixels = imzML.getHeight();
			nRows = imzML.getSpectrum(1, 1).getmzArray().length;

			nIterations = (int) totalXPixels * (int) totalYPixels;
			currentIteration = 1;
			currentXPixel = 1;
			currentYPixel = 1;

			// DataType is always the same.
			DataType type = (new DoubleCell(5)).getType();
			colSpec = new DataType[] { type };

			isTerminated = false;
			isFirstPass = false;
		}
		
		exec.checkCanceled();

		String[] names = new String[] { KeyLogic.getPixelKey(currentXPixel, currentYPixel) };
		DataTableSpec spec = new DataTableSpec(names, colSpec);

		/*
		 * Now we make the m/z table
		 */
		BufferedDataContainer bufMZ = exec.createDataContainer(spec);
		double[] mzArr = imzML.getSpectrum(currentXPixel, currentYPixel).getmzArray();
		for (int i = 0; i < nRows; i++) {
			DataCell[] cells = new DataCell[] { new DoubleCell(mzArr[i]) };
			DataRow row = new DefaultRow(new RowKey("" + i), cells);
			bufMZ.addRowToTable(row);
		}

		/*
		 * Now we make the intensity table
		 */
		BufferedDataContainer bufIntensity = exec.createDataContainer(spec);
		double[] intensityArr = imzML.getSpectrum(currentXPixel, currentYPixel).getIntensityArray();
		for (int i = 0; i < nRows; i++) {
			DataCell[] cells = new DataCell[] { new DoubleCell(intensityArr[i]) };
			DataRow row = new DefaultRow(new RowKey("" + i), cells);
			bufIntensity.addRowToTable(row);
		}

		// Iterate over the Pixels
		currentXPixel++;
		if (currentXPixel > totalXPixels) {
			currentXPixel = 1;
			currentYPixel++;
			if (currentYPixel > totalYPixels) {
				currentYPixel = 1;
			}
		}

		// Check if finished
		currentIteration++;
		if (currentIteration > nIterations) {
			isTerminated = true;
		}

		bufMZ.close();
		bufIntensity.close();

		return new BufferedDataTable[] { bufMZ.getTable(), bufIntensity.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO: generated method stub
		isFirstPass = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * No input tables so we are chilling.
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_file_chooser.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO: generated method stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO: generated method stub
	}

	@Override
	public boolean terminateLoop() {
		return isTerminated;
	}

}
