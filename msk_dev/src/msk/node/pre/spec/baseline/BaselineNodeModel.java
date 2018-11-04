package msk.node.pre.spec.baseline;

import java.io.File;
import java.io.IOException;

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
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.DataManipulator;
import msk.util.SettingsModelCreate;
import msk.util.preprocess.Baseline;

/**
 * This is the model implementation of Baseline. This node is used for baseline
 * subtraction in the preprocessing workflow.
 *
 * @author Andrew P Talbot
 */
public class BaselineNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(BaselineNodeModel.class);

	protected static final String BUTTON_GROUP_ID = "baseline_type_button_group";
	protected static final String WINDOW_ID = "window_size_number_edit";

	private SettingsModelString m_type = SettingsModelCreate.createSettingsModelString(BUTTON_GROUP_ID, "Minimum");
	private SettingsModelIntegerBounded m_window_size = SettingsModelCreate.createSettingsModelIntegerBounded(WINDOW_ID,
			2, 2, Integer.MAX_VALUE);

	private Baseline bs;
	private String baselineType;
	private int windowSize;
	private boolean firstPass = true;
	private DataTableSpec spec;

	/**
	 * Constructor for the node model.
	 */
	protected BaselineNodeModel() {
		/**
		 * Input 0 : intensity table
		 * 
		 * Output 0 : baseline Output 1 : baseline subtracted intensity
		 */
		super(1, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { inSpecs[0] };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		
		// First we check to see if the input data is null.
		long nSpectra = inData[0].size();
		if (nSpectra < 1) {
			logger.info("The number of incoming spectra is less than 1, so return an empty table.");
			return new BufferedDataTable[] { inData[0], inData[0] };
		}

		if (firstPass) {
			this.baselineType = m_type.getStringValue();
			this.windowSize = m_window_size.getIntValue();
			// Create the return objects.
			spec = inData[0].getDataTableSpec();
			logger.info("The datatable specs have been set.");

			firstPass = false;
			bs = new Baseline(this.baselineType, this.windowSize);
		}

		BufferedDataContainer bufBase = exec.createDataContainer(spec);
		BufferedDataContainer bufSpectra = exec.createDataContainer(spec);

		double spectraCounter = 0;
		// Loop through each row in the incoming table.
		for (DataRow row : inData[0]) {

			// Check the execution status, and set the execution progress.
			exec.checkCanceled();
			exec.setProgress(spectraCounter / nSpectra);

			bs.setSpectrum(DataManipulator.createRowArray(row));
			bufSpectra.addRowToTable(DataManipulator.createDataRow(bs.getBaselineSubtracted(), row.getKey()));
			bufBase.addRowToTable(DataManipulator.createDataRow(bs.getBaseline(), row.getKey()));
			
			// Add one to the spectra counter.
			spectraCounter++;
		}

		// Close and return
		bufBase.close();
		bufSpectra.close();

		return new BufferedDataTable[] { bufBase.getTable(), bufSpectra.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		firstPass = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_type.saveSettingsTo(settings);
		m_window_size.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_type.loadSettingsFrom(settings);
		m_window_size.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_type.validateSettings(settings);
		m_window_size.validateSettings(settings);
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
