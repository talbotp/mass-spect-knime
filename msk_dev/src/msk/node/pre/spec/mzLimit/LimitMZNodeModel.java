package msk.node.pre.spec.mzLimit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.data.container.ColumnRearranger;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;

import msk.util.DataManipulator;
import msk.util.SettingsModelCreate;

/**
 * This is the model implementation of LimitMColumnMZ. This node is used for
 * limitting the m/z values, it takes in imzML datasets and then outputs the
 * same tables but strictly within the range of the m/z specified by the user.
 *
 * @author Andrew P Talbot
 */
public class LimitMZNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(LimitMZNodeModel.class);

	// Create the unique String identifiers for the user input.
	protected static final String DOUBLE_MIN_ID = "double_min_m/z_number_edit";
	protected static final String DOUBLE_MAX_ID = "double_max_m/z_number_edit";

	// Create the SettingsModels for the user input
	private final SettingsModelDouble m_min_mz = SettingsModelCreate.createSettingsModelDouble(DOUBLE_MIN_ID, -1);
	private final SettingsModelDouble m_max_mz = SettingsModelCreate.createSettingsModelDouble(DOUBLE_MAX_ID, -1);

	private boolean firstPass;

	// Declare the user settings fields, init in configure().
	private double minMZ;
	private double maxMZ;
	private double[] mzVals;

	/**
	 * Constructor for the node model.
	 * 
	 * 2 input ports 2 output ports
	 */
	protected LimitMZNodeModel() {
		/**
		 * The ports are as follows: input 0: the unlimited m/z table input 1:
		 * the unlimited intenisty table
		 * 
		 * output 0: the m/z table with limits applied on m/z output 1: the
		 * intensity table with limits applied on m/z
		 */
		super(2, 2);
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

		// Check if data is empty.
		long nSpectra = inData[0].size();
		if (nSpectra < 1) {
			logger.info("The number of incoming spectra is less than 1, so return an empty table.");
			return new BufferedDataTable[] { inData[0], inData[0] };
		}

		// Check cancelled, difficult to set progress so this is all we do.
		exec.checkCanceled();

		// Set the fields in first pass of data.
		if (firstPass) {
			minMZ = m_min_mz.getDoubleValue();
			maxMZ = m_max_mz.getDoubleValue();

			CloseableRowIterator iter = inData[0].iterator();
			mzVals = DataManipulator.createRowArray(iter.next());
			iter.close();

			firstPass = false;
		}

		// Handle the extreme case of empty values.
		if (minMZ > mzVals[mzVals.length - 1] || maxMZ < mzVals[0])
			return new BufferedDataTable[] { DataManipulator.createEmptyTable(exec),
					DataManipulator.createEmptyTable(exec) };

		// Handle the case where all data is considered.
		if (minMZ <= mzVals[0] && maxMZ >= mzVals[mzVals.length - 1])
			return new BufferedDataTable[] { inData[0], inData[1] };

		// Get an array of values to keep.
		ArrayList<Integer> toKeep = new ArrayList<>();
		for (int i = 0; i < mzVals.length; i++) {
			double tmp = mzVals[i];
			if (tmp < minMZ)
				continue;
			else if (tmp > maxMZ)
				break;
			toKeep.add(i);
		}

		int[] arrToKeep = new int[toKeep.size()];
		for (int i = 0; i < arrToKeep.length; i++) {
			arrToKeep[i] = toKeep.get(i);
		}

		// Rearrange the incoming data according to the array of indexed values
		// to keep.
		ColumnRearranger col0 = new ColumnRearranger(inData[0].getDataTableSpec());
		col0.keepOnly(arrToKeep);
		BufferedDataTable mzTable = exec.createColumnRearrangeTable(inData[0], col0, exec);

		ColumnRearranger col1 = new ColumnRearranger(inData[1].getDataTableSpec());
		col1.keepOnly(arrToKeep);
		BufferedDataTable intensityTable = exec.createColumnRearrangeTable(inData[1], col1, exec);

		return new BufferedDataTable[] { mzTable, intensityTable };
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
		m_min_mz.saveSettingsTo(settings);
		m_max_mz.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_min_mz.loadSettingsFrom(settings);
		m_max_mz.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_min_mz.validateSettings(settings);
		m_max_mz.validateSettings(settings);
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
