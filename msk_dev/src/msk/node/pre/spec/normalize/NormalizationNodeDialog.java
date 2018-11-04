package msk.node.pre.spec.normalize;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "SpectrumNormalization" Node. This node is
 * used to normalize spectra in the body of a loop
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class NormalizationNodeDialog extends DefaultNodeSettingsPane {

	// Create the Strings to be displayed on the dialog.
	private final boolean D_BUTTON_VERT = true;
	private final String D_LABEL = "Normalization Type :";
	private final String[] D_BUTTON_EL = new String[] { "TIC", "Median", "Euclidean Norm" };

	private DialogComponent rowColButtonGroup = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(NormalizationNodeModel.BUTTON_GROUP_ID, "TIC"), D_BUTTON_VERT,
			D_LABEL, D_BUTTON_EL);

	/**
	 * New pane for configuring the SpectrumNormalization node.
	 */
	protected NormalizationNodeDialog() {
		addDialogComponent(rowColButtonGroup);
	}
}
