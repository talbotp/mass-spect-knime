package msk.node.read.imzML.loop.processed;

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
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.LoopStartNodeTerminator;

import msk.util.DataManipulator;
import msk.util.GenericReaderViewPanel;
import msk.util.ImzMLWrapper;
import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;
import msk.util.preprocess.Rebinner;
import mzML.Spectrum;

/**
 * This is the model implementation of ImzMLProcessedLoopStart. Node which is
 * used to read in processed imzML datasets.
 *
 * @author Andrew P Talbot
 */
public class ImzMLProcessedLoopStartNodeModel extends NodeModel implements LoopStartNodeTerminator {

	private NodeLogger logger = NodeLogger.getLogger(ImzMLProcessedLoopStartNodeModel.class);

	public static final String N_SPECTRA_ID = "number_of_spectra_per_iter_ID";
	public static final String FILE_CHOOSER_ID = "file_chooser_ID";
	public static final String MIN_MZ_SIZE_ID = "Min_mz_value_ID";
	public static final String MAX_MZ_SIZE_ID = "Max_mz_value_ID";
	public static final String BIN_SIZE_ID = "bin_size_ID";

	// Generic Reader SettingsModels
	private SettingsModelIntegerBounded m_smsIB = SettingsModelCreate
			.createSettingsModelIntegerBounded(ImzMLProcessedLoopStartNodeModel.N_SPECTRA_ID, 1, 1, Integer.MAX_VALUE);
	private SettingsModelString m_sms = SettingsModelCreate
			.createSettingsModelString(ImzMLProcessedLoopStartNodeModel.FILE_CHOOSER_ID, null);

	// Rebinner SettingsModels
	private SettingsModelDoubleBounded m_min_mz = SettingsModelCreate
			.createSettingsModelDoubleBounded(ImzMLProcessedLoopStartNodeModel.MIN_MZ_SIZE_ID, 0, 0, Double.MAX_VALUE);
	private SettingsModelDoubleBounded m_max_mz = SettingsModelCreate
			.createSettingsModelDoubleBounded(ImzMLProcessedLoopStartNodeModel.MAX_MZ_SIZE_ID, 0, 0, Double.MAX_VALUE);
	private SettingsModelDoubleBounded m_bin_size = SettingsModelCreate
			.createSettingsModelDoubleBounded(ImzMLProcessedLoopStartNodeModel.BIN_SIZE_ID, 1, 0.01, Double.MAX_VALUE);

	private boolean isTerminated;
	private boolean isFirstPass;
	private ImzMLWrapper imzMLReader;
	private Iterator<Spectrum> specIter;
	private int nSpectraPerIter;
	private DataTableSpec spec;
	private double[] rebinMZ;

	/**
	 * Constructor for the node model.
	 */
	protected ImzMLProcessedLoopStartNodeModel() {
		super(0, 2);
		/*
		 * m/z out of port 0 and intensity out of port 1.
		 */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
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
				imzMLReader = new ImzMLWrapper(m_sms.getStringValue());
			} catch (IllegalArgumentException e) {
				logger.fatal("We could not load that imzML file.");
			}

			specIter = imzMLReader.iterator();
			nSpectraPerIter = m_smsIB.getIntValue();

			rebinMZ = Rebinner.generateNewAxis(m_min_mz.getDoubleValue(), m_max_mz.getDoubleValue(),
					m_bin_size.getDoubleValue());

			DataType doubleType = DoubleCell.TYPE;
			String[] colNames = new String[rebinMZ.length];
			DataType[] colTypes = new DataType[rebinMZ.length];
			for (int i = 0; i < rebinMZ.length; i++) {
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

			double[] intensity = Rebinner.generateBinnedIntensityArray(spec.getmzArray(), spec.getIntensityArray(),
					rebinMZ, m_bin_size.getDoubleValue());

			intensityTable.addRowToTable(DataManipulator.createDataRow(intensity, rowKey));
			
			mzTable.addRowToTable(DataManipulator.createDataRow(rebinMZ, rowKey));
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

	@Override
	public boolean terminateLoop() {
		return isTerminated;
	}
	
	/**
	 * Create the view for the ImzML.
	 */
	public JPanel getView() {
		if (imzMLReader != null)
			return new GenericReaderViewPanel(imzMLReader.getImzML(), m_sms.getStringValue());
		else
			return new JPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		isTerminated = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_smsIB.saveSettingsTo(settings);
		m_sms.saveSettingsTo(settings);
		m_min_mz.saveSettingsTo(settings);
		m_max_mz.saveSettingsTo(settings);
		m_bin_size.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_smsIB.loadSettingsFrom(settings);
		m_sms.loadSettingsFrom(settings);
		m_min_mz.loadSettingsFrom(settings);
		m_max_mz.loadSettingsFrom(settings);
		m_bin_size.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_smsIB.validateSettings(settings);
		m_sms.validateSettings(settings);
		m_min_mz.validateSettings(settings);
		m_max_mz.validateSettings(settings);
		m_bin_size.validateSettings(settings);
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
