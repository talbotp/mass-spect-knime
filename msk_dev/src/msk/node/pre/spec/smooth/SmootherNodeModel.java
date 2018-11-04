package msk.node.pre.spec.smooth;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
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
import msk.util.preprocess.Smoother;

/**
 * This is the model implementation of SavitzkyGolaySmoother. This node is to be
 * used for smoothing a set of rows or a set of columns using Savitzky-Golay
 * smoothing, using a filter width specified by the user in the dialog.
 *
 * @author Andrew P Talbot
 * @version 08/07/2018
 */
public class SmootherNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(SmootherNodeModel.class);

	protected static final String FILTER_WIDTH_ID = "filter_width_Number_Edit";
	protected static final String TYPE_ID = "smooth_type_button_group";

	private SettingsModelIntegerBounded m_filter_width = SettingsModelCreate
			.createSettingsModelIntegerBounded(FILTER_WIDTH_ID, 2, 2, Integer.MAX_VALUE);
	private SettingsModelString m_type = SettingsModelCreate.createSettingsModelString(TYPE_ID, null);

	private boolean isFirstPass = true;
	private int typeSmooth;
	private int filterWidth;

	private DataTableSpec spec;

	/**
	 * Constructor for the node model.
	 */
	protected SmootherNodeModel() {
		/*
		 * One input port is the incoming DataTable to smooth. One output port
		 * is the outgoing smoothed DataTable.
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

		// Set fields in first pass.
		if (isFirstPass) {
			filterWidth = m_filter_width.getIntValue();

			String tmp = m_type.getStringValue();
			if (tmp.equals("Savitzky-Golay"))
				typeSmooth = Smoother.SAV_GOLAY;
			else if (tmp.equals("Moving Mean"))
				typeSmooth = Smoother.MOVING_MEAN;
			else if (tmp.equals("Triangular Moving Mean"))
				typeSmooth = Smoother.TRIANG_MOVING_MEAN;
			else
				logger.fatal("That isn't a valid method for smoothing.");

			// Set the Table values
			spec = inData[1].getDataTableSpec();
			isFirstPass = false;

		}

		// Create outgoing objects.
		BufferedDataContainer smoothedTable = exec.createDataContainer(spec);

		// Loop through each row and Smooth it.
		CloseableRowIterator mziter = inData[0].iterator();
		CloseableRowIterator intensityiter = inData[1].iterator();

		int spectraCounter = 0;
		// Smooth every row of data.
		while (mziter.hasNext() && intensityiter.hasNext()) {

			// Set the execution progress
			exec.checkCanceled();
			exec.setProgress(spectraCounter / nSpectra);

			DataRow mzRow = mziter.next();
			DataRow intensityRow = intensityiter.next();

			double[] mz = DataManipulator.createRowArray(mzRow);
			double[] intensity = DataManipulator.createRowArray(intensityRow);

			// Smooth the intensity array according to the method we use.
			switch (typeSmooth) {
			case Smoother.SAV_GOLAY:
				intensity = Smoother.savitzkyGolaySmooth(mz, intensity, filterWidth);
				break;
			case Smoother.MOVING_MEAN:
				intensity = Smoother.movingMeanSmooth(intensity, filterWidth);
				break;
			case Smoother.TRIANG_MOVING_MEAN:
				intensity = Smoother.triangularMovingMean(intensity, filterWidth);
				break;
			default:
				logger.fatal("That is not a valid smoothing type.");
				break;
			}

			// Remove negatives as we can sometimes get negative results.
			intensity = msk.util.MathMSK.removeNegatives(intensity);
			smoothedTable.addRowToTable(DataManipulator.createDataRow(intensity, intensityRow.getKey()));
			spectraCounter++;
		}

		// Close iterators, container and return the tables.
		mziter.close();
		intensityiter.close();

		smoothedTable.close();

		return new BufferedDataTable[] { inData[0], smoothedTable.getTable() };
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
		m_filter_width.saveSettingsTo(settings);
		m_type.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_filter_width.loadSettingsFrom(settings);
		m_type.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_filter_width.validateSettings(settings);
		m_type.validateSettings(settings);
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
