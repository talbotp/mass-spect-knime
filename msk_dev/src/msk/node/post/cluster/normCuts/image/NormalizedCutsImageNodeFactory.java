package msk.node.post.cluster.normCuts.image;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.node.post.cluster.normCuts.NormalizedCutsNodeModel;
import msk.util.SettingsModelCreate;
import msk.util.cluster.GenericNormCutsDialog;

/**
 * <code>NodeFactory</code> for the "NormalizedCutsImage" Node. Perform
 * Normalized Cuts clustering on an image.
 *
 * @author NormalizedCutsImage
 */
public class NormalizedCutsImageNodeFactory extends NodeFactory<NormalizedCutsImageNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NormalizedCutsImageNodeModel createNodeModel() {
		return new NormalizedCutsImageNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<NormalizedCutsImageNodeModel> createNodeView(final int viewIndex,
			final NormalizedCutsImageNodeModel nodeModel) {
		return new NormalizedCutsImageNodeView(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasDialog() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeDialogPane createNodeDialogPane() {
		SettingsModelString m_imgFileChooser = SettingsModelCreate
				.createSettingsModelString(NormalizedCutsImageNodeModel.IMG_FILE_ID, null);
		SettingsModelDoubleBounded m_sigmaI = SettingsModelCreate
				.createSettingsModelDoubleBounded(NormalizedCutsImageNodeModel.SIGMAI_ID, 1, 0, Integer.MAX_VALUE);
		SettingsModelDoubleBounded m_sigmaX = SettingsModelCreate
				.createSettingsModelDoubleBounded(NormalizedCutsImageNodeModel.SIGMAX_ID, 1, 0, Integer.MAX_VALUE);
		SettingsModelDoubleBounded m_R = SettingsModelCreate
				.createSettingsModelDoubleBounded(NormalizedCutsImageNodeModel.R_ID, 1, 0, Integer.MAX_VALUE);
		SettingsModelString m_P_TYPE = SettingsModelCreate
				.createSettingsModelString(NormalizedCutsImageNodeModel.PARTITION_TYPE_ID, null);
		SettingsModelDoubleBounded m_L = SettingsModelCreate
				.createSettingsModelDoubleBounded(NormalizedCutsImageNodeModel.L_ID, 0.1, 0, Integer.MAX_VALUE);
		SettingsModelDoubleBounded m_cut_threshold = SettingsModelCreate.createSettingsModelDoubleBounded(
				NormalizedCutsImageNodeModel.NCUT_THRESHOLD_ID, 0.2, 0, Integer.MAX_VALUE);
		SettingsModelIntegerBounded m_max_cluster = SettingsModelCreate.createSettingsModelIntegerBounded(
				NormalizedCutsImageNodeModel.MAX_CLUSTER_ID, 1, 1, Integer.MAX_VALUE);
		// Lanczos Values go here.
		SettingsModelDoubleBounded m_accuracy = SettingsModelCreate
				.createSettingsModelDoubleBounded(NormalizedCutsNodeModel.LANCZOS_ACCURACY, 0.001, 0, Double.MAX_VALUE);
		SettingsModelString m_comparison = SettingsModelCreate
				.createSettingsModelString(NormalizedCutsNodeModel.LANCZOS_USE_EIGENVECTORS, "Eigenvectors");

		return new GenericNormCutsDialog(m_imgFileChooser, m_sigmaI, m_sigmaX, m_R, m_P_TYPE, m_L, m_cut_threshold,
				m_max_cluster, m_accuracy, m_comparison);
	}

}
