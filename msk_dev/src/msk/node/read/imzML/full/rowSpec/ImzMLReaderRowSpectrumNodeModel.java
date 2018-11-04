package msk.node.read.imzML.full.rowSpec;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JPanel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
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

import msk.util.DataManipulator;
import msk.util.GenericReaderViewPanel;
import msk.util.ImzMLWrapper;
import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;
import mzML.Spectrum;

/**
 * This is the model implementation of ImzMLReaderColumnMZ. This is a reader for
 * imzML datasets which reads in the whole dataset where columns are the mz, and
 * the rows are the pixels.
 *
 * @author Andrew P Talbot
 * @version 28/06/2018
 */
public class ImzMLReaderRowSpectrumNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(ImzMLReaderRowSpectrumNodeModel.class);

	protected static final String FILE_CHOOSER_ID = "file_chooser_id_imzML";

	private final SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID,
			null);

	private ImzMLWrapper imzMLReader;

	/**
	 * Constructor for the node model.
	 */
	protected ImzMLReaderRowSpectrumNodeModel() {
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * Nothing to do here as there are no incoming data tables :)
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		// Init the reader.
		try {
			imzMLReader = new ImzMLWrapper(m_file_chooser.getStringValue());
		} catch (IllegalArgumentException e) {
			logger.fatal("We could not load that imzML file, see error frame.");
		}

		// Init the datatable spec.
		int nColumns = imzMLReader.getImzML().getSpectrum(1, 1).getmzArray().length;

		DataType doubleType = DoubleCell.TYPE;
		String[] colNames = new String[nColumns];
		DataType[] colTypes = new DataType[nColumns];
		for (int i = 0; i < nColumns; i++) {
			colNames[i] = "" + i;
			colTypes[i] = doubleType;
		}
		DataTableSpec spec = new DataTableSpec(colNames, colTypes);
		
		// Get the total number of pixels.
		int nSpectra = imzMLReader.getImzML().getWidth() * imzMLReader.getImzML().getHeight();

		// Now we init the container using the spec.
		BufferedDataContainer mzTable = exec.createDataContainer(spec);
		BufferedDataContainer intensityTable = exec.createDataContainer(spec);

		double spectraCounter = 0;
		// Now we make the iterator and add the rows to the table.
		Iterator<Spectrum> specIter = imzMLReader.iterator();
		while (specIter.hasNext()) {
			
			// Set the execution progress.
			exec.checkCanceled();
			exec.setProgress(spectraCounter / nSpectra);
			
			Spectrum spectra = specIter.next();
			double[] mz = spectra.getmzArray();
			double[] intensity = spectra.getIntensityArray();
			
			RowKey rowKey = new RowKey(
					(new KeyLogic(imzMLReader.getCurrentXPixel(), imzMLReader.getCurrentYPixel()).toString()));
			
			mzTable.addRowToTable(DataManipulator.createDataRow(mz, rowKey));
			intensityTable.addRowToTable(DataManipulator.createDataRow(intensity, rowKey));
			
			spectraCounter++;
		}
		
		// Close the containers are return the table.
		mzTable.close();
		intensityTable.close();
		
		return new BufferedDataTable[] { mzTable.getTable(), intensityTable.getTable() };
	}
	
	/**
	 * Create the view for the ImzML.
	 */
	public JPanel getView() {
		if (imzMLReader != null)
			return new GenericReaderViewPanel(imzMLReader.getImzML(), m_file_chooser.getStringValue());
		else
			return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Nothing to reset.
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
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	
	}

}
