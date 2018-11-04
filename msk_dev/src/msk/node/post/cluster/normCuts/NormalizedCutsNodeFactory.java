package msk.node.post.cluster.normCuts;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.node.post.cluster.normCuts.image.NormalizedCutsImageNodeModel;
import msk.util.SettingsModelCreate;
import msk.util.cluster.GenericNormCutsDialog;

/**
 * <code>NodeFactory</code> for the "NormalizedCuts" Node. Node for performing
 * Normalized cuts algorithm on Spectral data, where the spectra are stored in
 * the rows of a DataTable.
 *
 * @author Andrew P Talbot
 */
public class NormalizedCutsNodeFactory extends NodeFactory<NormalizedCutsNodeModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NormalizedCutsNodeModel createNodeModel() {
		return new NormalizedCutsNodeModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNrNodeViews() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NodeView<NormalizedCutsNodeModel> createNodeView(final int viewIndex,
			final NormalizedCutsNodeModel nodeModel) {
		return null;
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

		// No file chooser in this one so the first parameter is a null.
		return new GenericNormCutsDialog(null, m_sigmaI, m_sigmaX, m_R, m_P_TYPE, m_L, m_cut_threshold, m_max_cluster,
				m_accuracy, m_comparison);
	}

}
