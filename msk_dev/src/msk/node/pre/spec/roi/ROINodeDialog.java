package msk.node.pre.spec.roi;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "IonImageRegionOfInterest" Node.
 * 
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author
 */
public class ROINodeDialog extends DefaultNodeSettingsPane {

	// Groups for the display on the dialog
	private final String GROUP_1 = "Limits on image width";
	private final String GROUP_2 = "Limits on image height";

	// Displays on the Dialog
	private final String D_MIN_W = "Minimum :";
	private final String D_MAX_W = "Maximum :";
	private final String D_MIN_H = "Minimum :";
	private final String D_MAX_H = "Maximum :";
	

	private DialogComponent minWidthDouble = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(ROINodeModel.INTEGER_MIN_WIDTH_ID, 1, 1, Integer.MAX_VALUE), D_MIN_W);

	private DialogComponent maxWidthDouble = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(ROINodeModel.INTEGER_MAX_WIDTH_ID, 1, 1, Integer.MAX_VALUE), D_MAX_W);

	private DialogComponent minHeightDouble = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(ROINodeModel.INTEGER_MIN_HEIGHT_ID, 1, 1, Integer.MAX_VALUE), D_MIN_H);

	private DialogComponent maxHeightDouble = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(ROINodeModel.INTEGER_MAX_HEIGHT_ID, 1, 1, Integer.MAX_VALUE), D_MAX_H);
	
	/**
	 * New pane for configuring IonImageRegionOfInterest node dialog. This is
	 * just a suggestion to demonstrate possible default dialog components.
	 */
	protected ROINodeDialog() {
		createNewGroup(GROUP_1);
		addDialogComponent(minWidthDouble);
		addDialogComponent(maxWidthDouble);
		
		createNewGroup(GROUP_2);
		addDialogComponent(minHeightDouble);
		addDialogComponent(maxHeightDouble);
		
		closeCurrentGroup();
	}
}
