package msk.node.post.cluster.som;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.KeyLogic;
import msk.util.SettingsModelCreate;
import msk.util.cluster.SOMWrapper;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

/**
 * This is the model implementation of SOM. This node is useful for performing
 * Self organizng map clustering in KNIIME.
 *
 * @author Andrew P Talbot
 */
public class SOMNodeModel extends NodeModel {

	protected static final String GRID_TYPE_ID = "grid_type_id";
	protected static final String LEARN_TYPE_ID = "learn_type_id";
	protected static final String NHB_TYPE_ID = "neighborhood_type_id";
	protected static final String DIST_TYPE_ID = "dist_type_id";
	protected static final String LEARN_RATE_ID = "learn_rate_id";
	protected static final String INITIAL_RADIUS_ID = "intial_radius_size_id";
	protected static final String N_ITERATIONS_ID = "number_iterations_id";
	protected static final String N_X_DIM_ID = "n_x_id";
	protected static final String N_Y_DIM_ID = "n_y_id";

	private SettingsModelString m_grid = SettingsModelCreate.createSettingsModelString(SOMNodeModel.GRID_TYPE_ID,
			"Hexagonal");
	private SettingsModelString m_learn = SettingsModelCreate.createSettingsModelString(SOMNodeModel.LEARN_TYPE_ID,
			"Exponential");
	private SettingsModelString m_distance = SettingsModelCreate.createSettingsModelString(SOMNodeModel.DIST_TYPE_ID,
			"Euclidean");
	private SettingsModelString m_nhb_type = SettingsModelCreate.createSettingsModelString(SOMNodeModel.NHB_TYPE_ID,
			"Gaussian");
	private SettingsModelDoubleBounded m_learnRate = SettingsModelCreate
			.createSettingsModelDoubleBounded(SOMNodeModel.LEARN_RATE_ID, 0.1, 0, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_init_radius = SettingsModelCreate
			.createSettingsModelIntegerBounded(SOMNodeModel.INITIAL_RADIUS_ID, 8, 1, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_n_iter = SettingsModelCreate
			.createSettingsModelIntegerBounded(SOMNodeModel.N_ITERATIONS_ID, 1000, 1, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_n_x = SettingsModelCreate
			.createSettingsModelIntegerBounded(SOMNodeModel.N_X_DIM_ID, 2, 1, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_n_y = SettingsModelCreate
			.createSettingsModelIntegerBounded(SOMNodeModel.N_Y_DIM_ID, 2, 1, Integer.MAX_VALUE);

	private int gridType;
	private int learnType;
	private int distanceType;
	private int neighborhood;
	private int nCols;

	/**
	 * Constructor for the node model.
	 */
	protected SOMNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		setFields(inData[0]);

		// Make the SOM Object.
		SOMWrapper som = new SOMWrapper(m_n_x.getIntValue(), m_n_y.getIntValue(), this.gridType, m_n_iter.getIntValue(),
				m_learnRate.getDoubleValue(), m_init_radius.getIntValue(), this.learnType, this.neighborhood,
				this.distanceType);

		som.setDataSet(inData[0]);
		som.cluster();
		Dataset[] clusters = som.getSOMClusters();

		// Make the new DataTableSpec, which is one larger than the size of the
		// incoming DataTable, it is essentially the incoming table with the
		// cluster values appended as a column.
		String[] names = inData[0].getDataTableSpec().getColumnNames();
		String[] newNames = new String[names.length + 1];

		for (int i = 0; i < names.length; i++)
			newNames[i] = names[i];
		newNames[newNames.length - 1] = "cluster";

		DataType[] types = new DataType[newNames.length];
		for (int i = 0; i < types.length - 1; i++)
			types[i] = DoubleCell.TYPE;
		types[types.length - 1] = StringCell.TYPE;

		DataTableSpec spec = new DataTableSpec(newNames, types);
		BufferedDataContainer buf = exec.createDataContainer(spec);
		
		// Now we have created the outgoing DataTable, we add the values to it in a sorted fashion.
		
		// Initialize array of clusters. 
		ArrayList<Integer> clusterValues = new ArrayList<>();
		
		// Make sorted map to store a key and its cluster value.
		SortedMap<KeyLogic, Integer> clusterPixels = new TreeMap<>();
		
		int currentClusterValue = 0;
		for (Dataset d : clusters) {
			for (Instance inst : d) {
				clusterPixels.put(new KeyLogic((String) inst.classValue()), currentClusterValue);
			}
			currentClusterValue++;
		}
		
		clusterPixels.forEach((k,v) -> clusterValues.add(v));
		
		int[] clusterArray = new int[clusterValues.size()];
		
		for (int i = 0; i < clusterArray.length; i++) {
			clusterArray[i] = clusterValues.get(i);
		}
		
		KeyLogic[] keys = clusterPixels.keySet().toArray(new KeyLogic[0]);
		
		// Now we add the values to the cluster array.
		int nAdded = 0;
		for (DataRow row : inData[0]) {
			
			// Add row to new table.
			DataCell[] cells = new DataCell[types.length];
			int counter = 0;
			for (DataCell cell : row) {
				cells[counter] = cell;
				counter++;
			}
			cells[cells.length - 1] = new StringCell("Cluster_" + clusterArray[nAdded]);
			
			// Add the row
			buf.addRowToTable(new DefaultRow(keys[nAdded].toString(), cells));
			nAdded++;
		}
		
		buf.close();
		return new BufferedDataTable[] { buf.getTable() };
	}

	/**
	 * Add a row to the table table
	 * 
	 * @param table is the table we add the row to.
	 * @param instance is the data we add to the table in the row
	 * @param i is the cluster index of this instance.
	 */
	protected void addToTable(BufferedDataContainer table, Instance instance, int i) {
		DataCell[] cells = new DataCell[this.nCols + 1];
		int index = 0;
		for (Double d : instance) {
			cells[index] = new DoubleCell(d);
			index++;
		}
		cells[cells.length - 1] = new StringCell("cluster_" + i);
		table.addRowToTable(new DefaultRow(new RowKey(instance.classValue().toString()), cells));
	}

	/**
	 * Set the fields to make it easier to type.
	 */
	protected void setFields(BufferedDataTable table) {
		String gridStr = m_grid.getStringValue();
		if (gridStr.equals("Hexagonal"))
			gridType = SOMWrapper.GRIDTYPE_HEXAGONAL;
		else
			gridType = SOMWrapper.GRIDTYPE_RECTANGLES;

		String lrnStr = m_learn.getStringValue();
		if (lrnStr.equals("Exponential"))
			learnType = SOMWrapper.LEARNING_EXPONENTIAL;
		else if (lrnStr.equals("Inverse"))
			learnType = SOMWrapper.LEARNING_INVERSE;
		else
			learnType = SOMWrapper.LEARNING_LINEAR;

		String nhbStr = m_nhb_type.getStringValue();
		if (nhbStr.equals("Gaussian"))
			neighborhood = SOMWrapper.NEIGHBORHOOD_GAUSSIAN;
		else
			neighborhood = SOMWrapper.NEIGHBORHOOD_STEP;

		String distStr = m_distance.getStringValue();
		if (distStr.equals("Euclidean"))
			distanceType = SOMWrapper.DISTANCE_METRIC_EUCLIDEAN;
		else if (distStr.equals("Manhattan"))
			distanceType = SOMWrapper.DISTANCE_METRIC_MANHATTAN;
		else if (distStr.equals("Cosine"))
			distanceType = SOMWrapper.DISTANCE_METRIC_COSINE;
		else
			distanceType = SOMWrapper.DISTANCE_METRIC_NORM_DIST;

		this.nCols = table.getDataTableSpec().getNumColumns();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
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
		m_grid.saveSettingsTo(settings);
		m_learn.saveSettingsTo(settings);
		m_distance.saveSettingsTo(settings);
		m_nhb_type.saveSettingsTo(settings);
		m_learnRate.saveSettingsTo(settings);
		m_init_radius.saveSettingsTo(settings);
		m_n_iter.saveSettingsTo(settings);
		m_n_x.saveSettingsTo(settings);
		m_n_y.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_grid.loadSettingsFrom(settings);
		m_learn.loadSettingsFrom(settings);
		m_distance.loadSettingsFrom(settings);
		m_nhb_type.loadSettingsFrom(settings);
		m_learnRate.loadSettingsFrom(settings);
		m_init_radius.loadSettingsFrom(settings);
		m_n_iter.loadSettingsFrom(settings);
		m_n_x.loadSettingsFrom(settings);
		m_n_y.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_grid.validateSettings(settings);
		m_learn.validateSettings(settings);
		m_distance.validateSettings(settings);
		m_nhb_type.validateSettings(settings);
		m_learnRate.validateSettings(settings);
		m_init_radius.validateSettings(settings);
		m_n_iter.validateSettings(settings);
		m_n_x.validateSettings(settings);
		m_n_y.validateSettings(settings);
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
