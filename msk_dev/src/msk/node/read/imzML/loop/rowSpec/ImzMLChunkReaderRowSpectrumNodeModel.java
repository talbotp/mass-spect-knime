package msk.node.read.imzML.loop.rowSpec;

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
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.LoopStartNodeTerminator;

import msk.util.DataManipulator;
import msk.util.GenericReaderViewPanel;
import msk.util.ImzMLWrapper;
import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;
import mzML.Spectrum;

/**
 * This is the model implementation of ImzMLChunkReaderRowSpectrum. This node is
 * used for reading in imzML datasets where the rows are the spectra. It is
 * useful for large datasets.
 * 
 * Currently works ONLY for continuous imzML datasets.
 *
 * @author Andrew P Talbot
 * @version 29/06/2018
 */
public class ImzMLChunkReaderRowSpectrumNodeModel extends NodeModel implements LoopStartNodeTerminator {

	private NodeLogger logger = NodeLogger.getLogger(ImzMLChunkReaderRowSpectrumNodeModel.class);

	// Create the ID for the file chooser.
	protected final static String FILE_CHOOSER_ID = "file_chooser_id_imzML";
	protected final static String N_SPECTRA_ID = "n_spectra_per_iteration_id";

	// SetingsModel used in our model.
	protected final SettingsModelString m_file_chooser = SettingsModelCreate.createSettingsModelString(FILE_CHOOSER_ID,
			null);
	protected final SettingsModelIntegerBounded m_n_spectra = SettingsModelCreate
			.createSettingsModelIntegerBounded(N_SPECTRA_ID, 1, 1, Integer.MAX_VALUE);

	// Here are the fields used in our Model.
	private boolean isFirstPass = true;
	private boolean isTerminated = false;
	private DataTableSpec spec;
	private ImzMLWrapper imzMLReader;
	private Iterator<Spectrum> specIter;
	private int nSpectraPerIter;

	/**
	 * Constructor for the node model.
	 */
	protected ImzMLChunkReaderRowSpectrumNodeModel() {
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
		 * Nothing to configure here because no incoming data.
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		// On the first pass we can set the values of fields, so it isn't done
		// every loop.
		if (isFirstPass) {

			// Create the imzMLReader, spectrum iterator and the DataTable Spec.
			try {
				imzMLReader = new ImzMLWrapper(m_file_chooser.getStringValue());
			} catch (IllegalArgumentException e) {
				logger.fatal("We could not load that imzML file.");
			}

			specIter = imzMLReader.iterator();
			nSpectraPerIter = m_n_spectra.getIntValue();
			
			int nColumns = imzMLReader.getImzML().getSpectrum(1, 1).getmzArray().length;

			DataType doubleType = DoubleCell.TYPE;
			String[] colNames = new String[nColumns];
			DataType[] colTypes = new DataType[nColumns];
			for (int i = 0; i < nColumns; i++) {
				colNames[i] = "" + i;
				colTypes[i] = doubleType;
			}
			spec = new DataTableSpec(colNames, colTypes);

			isTerminated = false;
			isFirstPass = false;
		}

		BufferedDataContainer mzTable = exec.createDataContainer(spec);
		BufferedDataContainer intensityTable = exec.createDataContainer(spec);

		double spectraCounter = 0;
		// Loop through and 
		while (spectraCounter < nSpectraPerIter && specIter.hasNext()) {
			spectraCounter++;
			Spectrum spec = specIter.next();
			RowKey rowKey = new RowKey(
					(new KeyLogic(imzMLReader.getCurrentXPixel(), imzMLReader.getCurrentYPixel()).toString()));

			double[] mz = spec.getmzArray();
			mzTable.addRowToTable(DataManipulator.createDataRow(mz, rowKey));

			double[] intensity = spec.getIntensityArray();
			intensityTable.addRowToTable(DataManipulator.createDataRow(intensity, rowKey));

			// Report the progress so far.
			exec.checkCanceled();
			exec.setProgress(spectraCounter / nSpectraPerIter);
		}

		mzTable.close();
		intensityTable.close();

		// Check if final loop
		if (!specIter.hasNext())
			isTerminated = true;

		return new BufferedDataTable[] { mzTable.getTable(), intensityTable.getTable() };
	}
	
	/**
	 * Create the view for the ImzML.
	 */
	public JPanel getView() {
		if (imzMLReader != null)
			return new GenericReaderViewPanel(imzMLReader.getImzML(), m_file_chooser.getStringValue());
		else
			return new JPanel();
	}

	/**
	 * Getter so that we can access the imzMLReader in the node model in order
	 * to create the view on the data.
	 */
	public ImzMLWrapper getImzMLWrapper() {
		return imzMLReader;
	}
	
	public String getImzMLFile() {
		return m_file_chooser.getStringValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		isFirstPass = true;
	}

	/**
	 * Terminate the loop when there are no longer any spectra.
	 */
	@Override
	public boolean terminateLoop() {
		return isTerminated;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_file_chooser.saveSettingsTo(settings);
		m_n_spectra.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.loadSettingsFrom(settings);
		m_n_spectra.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_file_chooser.validateSettings(settings);
		m_n_spectra.validateSettings(settings);
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
