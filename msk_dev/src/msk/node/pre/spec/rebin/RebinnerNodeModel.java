package msk.node.pre.spec.rebin;

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
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;

import msk.util.DataManipulator;
import msk.util.SettingsModelCreate;
import msk.util.preprocess.Rebinner;

/**
 * This is the model implementation of SpectrumRebinner. This node is used for
 * rebinning mass spectrometry datasets.
 * 
 * NB :
 * 
 * This class only currently handles one row or one column at a time. Perhaps it
 * can be easily extended to work for multiple columns and multiple rows.
 *
 * @author Andrew P Talbot
 */
public class RebinnerNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(RebinnerNodeModel.class);

	protected static final String BUTTON_GROUP_ID = "row_column_button_group";
	protected static final String BIN_SIZE_ID = "bin_size_Number_Edit";
	protected static final String MIN_MZ_SIZE_ID = "min_mz_number_edit";
	protected static final String MAX_MZ_SIZE_ID = "max_mz_number_edit";

	private SettingsModelDoubleBounded m_bin_size = SettingsModelCreate.createSettingsModelDoubleBounded(BIN_SIZE_ID,
			1.0, 0.01, Double.MAX_VALUE);
	private SettingsModelDoubleBounded m_min_mz = SettingsModelCreate.createSettingsModelDoubleBounded(MIN_MZ_SIZE_ID,
			0, 0, Double.MAX_VALUE);
	private SettingsModelDoubleBounded m_max_mz = SettingsModelCreate.createSettingsModelDoubleBounded(MAX_MZ_SIZE_ID,
			0, 0, Double.MAX_VALUE);

	// These are altered in the isFirstPass block of code in execute.
	private boolean isFirstPass = true;
	private double binSize;
	private double minMZ;
	private double maxMZ;
	private DataType typeDouble;
	private DataTableSpec spec;

	private double[] newMZ;
	private int newMZLength;

	/**
	 * Constructor for the node model.
	 */
	protected RebinnerNodeModel() {
		/*
		 * In ports: 0-m/z 1-intensity Out ports: 0-new m/z 1-new intensity.
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

		// Initially set fields here.
		if (isFirstPass) {
			binSize = m_bin_size.getDoubleValue();
			minMZ = m_min_mz.getDoubleValue();
			maxMZ = m_max_mz.getDoubleValue();

			typeDouble = DoubleCell.TYPE;
			newMZ = Rebinner.generateNewAxis(minMZ, maxMZ, binSize);
			newMZLength = newMZ.length;

			// Get the DataTableSpec
			DataType[] colSpec = new DataType[newMZLength];
			String[] names = new String[newMZLength];

			for (int i = 0; i < newMZLength; i++) {
				colSpec[i] = typeDouble;
				names[i] = "" + i;
			}
			spec = new DataTableSpec(names, colSpec);

			isFirstPass = false;
		}
		
		// Create the outgoing BufferedDataContainers.
		BufferedDataContainer mzTable = exec.createDataContainer(spec);
		BufferedDataContainer intensityTable = exec.createDataContainer(spec);

		// Create the iterators to loop through every row in incoming data.
		CloseableRowIterator mziter = inData[0].iterator();
		CloseableRowIterator intensityiter = inData[1].iterator();

		double spectraCounter = 0;
		while (mziter.hasNext() && intensityiter.hasNext()) {

			// Check and set the execution progress.
			exec.checkCanceled();
			exec.setProgress(spectraCounter / nSpectra);

			DataRow mzRow = mziter.next();
			DataRow intensityRow = intensityiter.next();
			
			double[] mz = DataManipulator.createRowArray(mzRow);
			double[] intensity = DataManipulator.createRowArray(intensityRow);

			// Generate the new rebinned data.
			double[] binnedIntensity = Rebinner.generateBinnedIntensityArray(mz, intensity, newMZ, binSize);
			
			mzTable.addRowToTable(DataManipulator.createDataRow(newMZ, mzRow.getKey()));
			intensityTable.addRowToTable(DataManipulator.createDataRow(binnedIntensity, intensityRow.getKey()));
			
			spectraCounter++;
		}
		
		// Close the iterators
		mziter.close();
		intensityiter.close();
		
		// Close the containers and return the tables.
		mzTable.close();
		intensityTable.close();

		return new BufferedDataTable[] { mzTable.getTable(), intensityTable.getTable() };
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
		m_bin_size.saveSettingsTo(settings);
		m_min_mz.saveSettingsTo(settings);
		m_max_mz.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_bin_size.loadSettingsFrom(settings);
		m_min_mz.loadSettingsFrom(settings);
		m_max_mz.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_bin_size.validateSettings(settings);
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
