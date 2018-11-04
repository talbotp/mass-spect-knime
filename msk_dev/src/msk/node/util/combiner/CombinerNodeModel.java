package msk.node.util.combiner;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.container.CloseableRowIterator;
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

import msk.util.DataManipulator;

/**
 * This is the model implementation of IntensityMZColumnCombine. This node is
 * used to combine m/z and intensity values into one table. This is only to be
 * used if the data is continuous.
 *
 * @author Andrew P Talbot
 */
public class CombinerNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(CombinerNodeModel.class);

	private boolean firstPass = true;
	
	private DataTableSpec spec;

	/**
	 * Constructor for the node model.
	 */
	protected CombinerNodeModel() {
		/**
		 * 
		 */
		super(2, 1);
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

		// Set fields on first pass.
		if (firstPass) {
			
			// Set the m/z column array, so that we can set the DataTableSpec.
			CloseableRowIterator tmpiter = inData[0].iterator();
			double[] mz = DataManipulator.createRowArray(tmpiter.next());
			tmpiter.close();
			
			DataType type = DoubleCell.TYPE;
			DataType[] types = new DataType[mz.length];
			String[] cells = new String[mz.length];

			for (int i = 0; i < mz.length; i++) {
				types[i] = type;
				cells[i] = "" + mz[i];
			}

			spec = new DataTableSpec(cells, types);
		}

		BufferedDataContainer combinedTable = exec.createDataContainer(spec);

		double spectraCounter = 0;
		for (DataRow row : inData[1]) {
			spectraCounter++;
			
			// Check execution status and set progress.
			exec.checkCanceled();
			exec.setProgress(spectraCounter / nSpectra);
			
			// Add the row to the table.
			combinedTable.addRowToTable(row);
		}
		
		// Close the container and return the table.
		combinedTable.close();
		return new BufferedDataTable[] { combinedTable.getTable() };
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
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
