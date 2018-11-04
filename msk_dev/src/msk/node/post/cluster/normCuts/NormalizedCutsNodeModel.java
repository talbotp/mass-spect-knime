package msk.node.post.cluster.normCuts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
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

import msk.util.SettingsModelCreate;
import msk.util.cluster.NormalizedCuts;
import msk.util.cluster.NormalizedCutsSpectraMSK;

/**
 * This is the model implementation of NormalizedCuts. Node for performing
 * Normalized cuts algorithm on Spectral data, where the spectra are stored in
 * the rows of a DataTable.
 *
 * @author Andrew P Talbot
 */
public class NormalizedCutsNodeModel extends NodeModel {

	private NodeLogger logger = NodeLogger.getLogger(NormalizedCutsNodeModel.class);

	public static final String IMG_FILE_ID = "img_file_id";
	public static final String SIGMAI_ID = "sigma_i_id";
	public static final String SIGMAX_ID = "sigma_x_id";
	public static final String R_ID = "r_id";
	public static final String PARTITION_TYPE_ID = "p_type_id";
	public static final String L_ID = "l_id";
	public static final String NCUT_THRESHOLD_ID = "ncut_threshold_id";
	public static final String MAX_CLUSTER_ID = "max_cluster_id";

	// Lanczos values go here.
	public static final String LANCZOS_ACCURACY = "accuracy_id";
	public static final String LANCZOS_USE_EIGENVECTORS = "lanczos_use_eigenvectors";

	private SettingsModelDoubleBounded m_sigmaI = SettingsModelCreate.createSettingsModelDoubleBounded(SIGMAI_ID, 1, 0,
			Integer.MAX_VALUE);
	private SettingsModelDoubleBounded m_sigmaX = SettingsModelCreate.createSettingsModelDoubleBounded(SIGMAX_ID, 1, 0,
			Integer.MAX_VALUE);
	private SettingsModelDoubleBounded m_R = SettingsModelCreate.createSettingsModelDoubleBounded(R_ID, 1, 0,
			Integer.MAX_VALUE);
	private SettingsModelString m_P_TYPE = SettingsModelCreate.createSettingsModelString(PARTITION_TYPE_ID, null);
	private SettingsModelDoubleBounded m_L = SettingsModelCreate.createSettingsModelDoubleBounded(L_ID, 0.1, 0,
			Integer.MAX_VALUE);
	private SettingsModelDoubleBounded m_cut_threshold = SettingsModelCreate
			.createSettingsModelDoubleBounded(NCUT_THRESHOLD_ID, 0.2, 0, Integer.MAX_VALUE);
	private SettingsModelIntegerBounded m_max_cluster = SettingsModelCreate
			.createSettingsModelIntegerBounded(MAX_CLUSTER_ID, 1, 1, Integer.MAX_VALUE);

	// Lanczos Values go here.
	SettingsModelDoubleBounded m_accuracy = SettingsModelCreate
			.createSettingsModelDoubleBounded(NormalizedCutsNodeModel.LANCZOS_ACCURACY, 0.001, 0, Double.MAX_VALUE);
	SettingsModelString m_comparison = SettingsModelCreate
			.createSettingsModelString(NormalizedCutsNodeModel.LANCZOS_USE_EIGENVECTORS, "Eigenvectors");

	private NormalizedCuts nc;

	/**
	 * Constructor for the node model.
	 */
	protected NormalizedCutsNodeModel() {
		super(1, 1);
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

		printFields();

		// Set the cut type field.
		String type = m_P_TYPE.getStringValue();
		int actual_type = -1;
		switch (type) {
		case "Zero":
			actual_type = NormalizedCuts.PARTITION_BY_ZERO;
			break;
		case "Median":
			actual_type = NormalizedCuts.PARTITION_BY_MEDIAN;
			break;
		case "NCut":
			actual_type = NormalizedCuts.PARTITION_BY_NCUT;
			break;
		default:
			throw new IllegalStateException("That is not a valid value of actual_type = " + actual_type);
		}

		// Set value of use eigenvectors appropriately.
		boolean useEigvecs = false;
		String usevecsTMP = m_comparison.getStringValue();
		switch (usevecsTMP) {
		case "Eigenvectors":
			useEigvecs = true;
			break;
		case "Eigenvalues":
			useEigvecs = false;
			break;
		default:
			throw new IllegalStateException("That is not a valid value of usevecsTMP = " + usevecsTMP);
		}

		NormalizedCutsSpectraMSK nc = new NormalizedCutsSpectraMSK(m_sigmaI.getDoubleValue(), m_sigmaX.getDoubleValue(),
				m_R.getDoubleValue(), m_L.getDoubleValue(), m_cut_threshold.getDoubleValue(), actual_type,
				m_max_cluster.getIntValue(), m_accuracy.getDoubleValue(), useEigvecs, inData[0]);

		// Call the clustering algorithm.
		nc.clusterAndSetW();

		// Set the values of the cluster pixels.
		int[] clusters = nc.getclusters(true);

		// Make the new DataTableSpec, which is one larger than the size of the
		// incoming DataTable, it is essentially the incoming table with the
		// cluster values appended as a column.
		String[] names = inData[0].getDataTableSpec().getColumnNames();
		String[] newNames = new String[names.length + 1];

		for (int i = 0; i < names.length; i++)
			newNames[i] = names[i];
		newNames[newNames.length - 1] = "Cluster";

		DataType[] types = new DataType[newNames.length];
		for (int i = 0; i < types.length - 1; i++)
			types[i] = DoubleCell.TYPE;
		types[types.length - 1] = StringCell.TYPE;

		DataTableSpec spec = new DataTableSpec(newNames, types);
		BufferedDataContainer buf = exec.createDataContainer(spec);

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
			cells[cells.length - 1] = new StringCell("Cluster_" + clusters[nAdded]);

			// Add the row
			buf.addRowToTable(new DefaultRow(row.getKey(), cells));
			nAdded++;
		}
		
		buf.close();
		return new BufferedDataTable[] { buf.getTable() };
	}

	public void printFields() {
		logger.info("NOW WE PRINT USEFUL INFORMATION ABOUT THE NCUTS NODE.");
		logger.info("m_sigmaI = " + m_sigmaI.getDoubleValue());
		logger.info("m_sigmaX = " + m_sigmaX.getDoubleValue());
		logger.info("m_r = " + m_R.getDoubleValue());
		logger.info("Partition type = " + m_P_TYPE.getStringValue());
		logger.info("m_l = " + m_L.getDoubleValue());
		logger.info("Ncut Threshold = " + m_cut_threshold.getDoubleValue());
		logger.info("ax cluster = " + m_max_cluster.getIntValue());
		logger.info("lanczos accuracy = " + m_accuracy.getDoubleValue());
		logger.info("m_comparison = " + m_comparison.getStringValue());
		logger.info("WE ARE DONE PRINTING USEFUL VALUES FOR NCUTS NODE.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		nc = null;
	}

	/**
	 * Get the grayscale cluster image for this.
	 */
	public JLabel getClusterImage() {
		if (nc == null)
			return new JLabel();
		else {
			int[] clusters = nc.getclusters(true);

			BufferedImage image = NormalizedCuts.setImgPixelsGrayscale(
					new BufferedImage(nc.getImgWidth(), nc.getImgHeight(), BufferedImage.TYPE_BYTE_GRAY),
					NormalizedCuts.getClusterPixel(clusters, nc.getCurrentNClusters()));
			return new JLabel(new ImageIcon(image));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_sigmaI.saveSettingsTo(settings);
		m_sigmaX.saveSettingsTo(settings);
		m_R.saveSettingsTo(settings);
		m_P_TYPE.saveSettingsTo(settings);
		m_L.saveSettingsTo(settings);
		m_cut_threshold.saveSettingsTo(settings);
		m_max_cluster.saveSettingsTo(settings);
		m_accuracy.saveSettingsTo(settings);
		m_comparison.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_sigmaI.loadSettingsFrom(settings);
		m_sigmaX.loadSettingsFrom(settings);
		m_R.loadSettingsFrom(settings);
		m_P_TYPE.loadSettingsFrom(settings);
		m_L.loadSettingsFrom(settings);
		m_cut_threshold.loadSettingsFrom(settings);
		m_max_cluster.loadSettingsFrom(settings);
		m_accuracy.loadSettingsFrom(settings);
		m_comparison.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_sigmaI.validateSettings(settings);
		m_sigmaX.validateSettings(settings);
		m_R.validateSettings(settings);
		m_P_TYPE.validateSettings(settings);
		m_L.validateSettings(settings);
		m_cut_threshold.validateSettings(settings);
		m_max_cluster.validateSettings(settings);
		m_accuracy.validateSettings(settings);
		m_comparison.validateSettings(settings);
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
