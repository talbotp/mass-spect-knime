package msk.node.pre.spec.normalize;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
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
import msk.util.SettingsModelCreate;
import msk.util.preprocess.MSKSpectrum;
import msk.util.preprocess.Normalizer;
import msk.util.preprocess.PreprocessThreadUtil;
import msk.util.preprocess.PreprocessingThreadPool;

/**
 * This is the model implementation of SpectrumNormalization. This node is used
 * to normalize spectra in the body of a loop
 *
 * @author Andrew P Talbot
 */
public class NormalizationNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(NormalizationNodeModel.class);

	protected static final String BUTTON_GROUP_ID = "button_group_id";

	private SettingsModelString m_button_group = SettingsModelCreate.createSettingsModelString(BUTTON_GROUP_ID, "TIC");

	private boolean isFirstPass = true;
	private boolean concurrent;
	private DataTableSpec spec;

	private Normalizer normalizer;
	private String normalizationType;

	/**
	 * Constructor for the node model.
	 */
	protected NormalizationNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * specs are same as incoming data.
		 */
		return new DataTableSpec[] { inSpecs[0] };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		// Check if data is empty.
		long nSpectra = inData[0].size();
		if (nSpectra < 1) {
			logger.info("The number of incoming spectra is less than 1, so return an empty table.");
			return new BufferedDataTable[] { inData[0], inData[0] };
		}

		// Set the fields etc. here.
		if (isFirstPass) {
			normalizationType = m_button_group.getStringValue();
			spec = inData[0].getDataTableSpec();

			// Set the Normalization object with a stub array, this will be set
			// later on for each row in the incoming datatable.
			normalizer = new Normalizer(new double[0], normalizationType);

			concurrent = (nSpectra >= PreprocessingThreadPool.MIN_THREADS_TO_USE_POOL) ? true : false;

			isFirstPass = false;
		}

		// Make container here, add next then close at end of method before
		// return
		BufferedDataContainer intensityTable = exec.createDataContainer(spec);

		// Handle the concurrent cases seperately to the others.
		if (!concurrent) {
			double spectraCounter = 0;
			// Loop through all incoming data.
			for (DataRow row : inData[0]) {

				// Check the execution status, and set the execution progress.
				exec.checkCanceled();
				exec.setProgress(spectraCounter / nSpectra);

				double[] intensity = DataManipulator.createRowArray(row);

				normalizer.setSpectrum(intensity);
				normalizer.normalizeSpectrum();
				intensity = normalizer.getSpectrum();

				intensityTable.addRowToTable(DataManipulator.createDataRow(intensity, row.getKey()));

				// Increment to set the progress.
				spectraCounter++;
			}
		} else {

			// concurrent == true, so we make a ThreadPool
			
			List<MSKSpectrum> set = PreprocessThreadUtil.addAllRowsToMSKSpectrumList(inData[0]);
			PreprocessingThreadPool<Normalizer> threadPool = new PreprocessingThreadPool<>(new Normalizer(), "TIC",
					PreprocessingThreadPool.THREAD_COUNT_MEDIUM);
			threadPool.workThreads(set);
			threadPool.shutdown();
			
			PreprocessThreadUtil.AddRowsToDataContainer(intensityTable, threadPool.processedIntensity);
		}

		// Close and return the table.
		intensityTable.close();

		return new BufferedDataTable[] { intensityTable.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		isFirstPass = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_button_group.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_button_group.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_button_group.validateSettings(settings);
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
