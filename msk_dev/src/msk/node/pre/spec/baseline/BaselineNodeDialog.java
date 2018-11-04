package msk.node.pre.spec.baseline;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "Baseline" Node. This node is used for
 * baseline subtraction in the preprocessing workflow.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class BaselineNodeDialog extends DefaultNodeSettingsPane {

	private final boolean D_BUTTON_VERT = true;
	private final String D_LABEL = "Baseline type";
	private final String[] D_BUTTON_EL = new String[] { "Minimum", "Median", "Tophat", "Optimal Tophat" };

	private final String D_FILTER_WIDTH_LABEL = "Window Size / Structuring Element";

	private DialogComponent typeButtonGroup = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(BaselineNodeModel.BUTTON_GROUP_ID, "Minimum"), D_BUTTON_VERT,
			D_LABEL, D_BUTTON_EL);

	private DialogComponent windowNumberEdit = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(BaselineNodeModel.WINDOW_ID, 2, 2, Integer.MAX_VALUE),
			D_FILTER_WIDTH_LABEL);

	/**
	 * New pane for configuring the Baseline node.
	 */
	protected BaselineNodeDialog() {

		addDialogComponent(typeButtonGroup);

		addDialogComponent(windowNumberEdit);
	}

}
