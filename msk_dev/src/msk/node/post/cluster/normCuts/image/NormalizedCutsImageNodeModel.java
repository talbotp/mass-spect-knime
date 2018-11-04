package msk.node.post.cluster.normCuts.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.def.IntCell;
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

import msk.util.DataManipulator;
import msk.util.SettingsModelCreate;
import msk.util.cluster.NormalizedCuts;
import msk.util.cluster.NormalizedCutsGray;

/**
 * This is the model implementation of NormalizedCutsImage. Perform Normalized
 * Cuts clustering on an image.
 *
 * @author NormalizedCutsImage
 * 
 * TODO: FIX THIS NODE
 */
public class NormalizedCutsImageNodeModel extends NodeModel {

	public static final String IMG_FILE_ID = "img_file_id";
	public static final String SIGMAI_ID = "sigma_i_id";
	public static final String SIGMAX_ID = "sigma_x_id";
	public static final String R_ID = "r_id";
	public static final String PARTITION_TYPE_ID = "p_type_id";
	public static final String L_ID = "l_id";
	public static final String NCUT_THRESHOLD_ID = "ncut_threshold_id";
	public static final String MAX_CLUSTER_ID = "max_cluster_id";

	private SettingsModelString m_imgFileChooser = SettingsModelCreate.createSettingsModelString(IMG_FILE_ID, null);
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

	private NormalizedCuts nc;

	/**
	 * Constructor for the node model.
	 */
	protected NormalizedCutsImageNodeModel() {
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		/*
		 * No input so do nothing here.
		 */
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		double sigmaI = m_sigmaI.getDoubleValue();
		double sigmaX = m_sigmaX.getDoubleValue();
		double r = m_R.getDoubleValue();
		double l = m_L.getDoubleValue();
		double nCutThreshold = m_cut_threshold.getDoubleValue();

		String nCutType = m_P_TYPE.getStringValue();
		int nType;
		switch (nCutType) {
		case "Zero":
			nType = NormalizedCuts.PARTITION_BY_ZERO;
			break;
		case "Median":
			nType = NormalizedCuts.PARTITION_BY_MEDIAN;
			break;
		case "NCut":
			nType = NormalizedCuts.PARTITION_BY_NCUT;
			break;
		default:
			nType = -1000;
		}

		if (nType == -1000)
			throw new InvalidSettingsException("Invalid value of Partition type.");

		int nClusters = m_max_cluster.getIntValue();
		String imgPath = m_imgFileChooser.getStringValue();

//		nc = new NormalizedCutsGray(sigmaI, sigmaX, r, l, nCutThreshold, nType, nClusters, imgPath);
//		nc.clusterAndSetW();
//
//		int[] arr = nc.getclusters(true);

		String[] names = { "Clusters" };
		DataType[] types = { IntCell.TYPE };

		DataTableSpec spec = new DataTableSpec(names, types);
		BufferedDataContainer tab = exec.createDataContainer(spec);

//		DataManipulator.addColumn(tab, arr);

		tab.close();

		return new BufferedDataTable[] { tab.getTable() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// Do nothing.
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
		m_imgFileChooser.saveSettingsTo(settings);
		m_sigmaI.saveSettingsTo(settings);
		m_sigmaX.saveSettingsTo(settings);
		m_R.saveSettingsTo(settings);
		m_P_TYPE.saveSettingsTo(settings);
		m_L.saveSettingsTo(settings);
		m_cut_threshold.saveSettingsTo(settings);
		m_max_cluster.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_imgFileChooser.loadSettingsFrom(settings);
		m_sigmaI.loadSettingsFrom(settings);
		m_sigmaX.loadSettingsFrom(settings);
		m_R.loadSettingsFrom(settings);
		m_P_TYPE.loadSettingsFrom(settings);
		m_L.loadSettingsFrom(settings);
		m_cut_threshold.loadSettingsFrom(settings);
		m_max_cluster.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		m_imgFileChooser.validateSettings(settings);
		m_sigmaI.validateSettings(settings);
		m_sigmaX.validateSettings(settings);
		m_R.validateSettings(settings);
		m_P_TYPE.validateSettings(settings);
		m_L.validateSettings(settings);
		m_cut_threshold.validateSettings(settings);
		m_max_cluster.validateSettings(settings);
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
