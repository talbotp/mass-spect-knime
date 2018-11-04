package msk.node.pre.spec.mzLimit;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "LimitMColumnMZ" Node. This node is used for
 * limitting the m/z values, it takes in imzML datasets and then outputs the
 * same tables but strictly within the range of the m/z specified by the user.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class LimitMZNodeDialog extends DefaultNodeSettingsPane {

	// Groups for the display on the dialog
	private final String GROUP_1 = "Limits on m/z";
	
	// Displays on the Dialog
	private final String D_MZ_MIN = "Minimum:";
	private final String D_MZ_MAX = "Maximum:";
	
	private DialogComponent minMZDouble = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelDouble(LimitMZNodeModel.DOUBLE_MIN_ID, -1), D_MZ_MIN);

	private DialogComponent maxMZDouble = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelDouble(LimitMZNodeModel.DOUBLE_MAX_ID, -1), D_MZ_MAX);
	
	/**
	 * New pane for configuring the LimitMColumnMZ node.
	 */
	protected LimitMZNodeDialog() {
		createNewGroup(GROUP_1);
		addDialogComponent(minMZDouble);
		addDialogComponent(maxMZDouble);
		closeCurrentGroup();
	}
}
