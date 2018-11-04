package msk.node.viz.image;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "MZViewer" Node.
 * Node is used for viewing images in grayscale for m/z channels.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class MZViewerNodeDialog extends DefaultNodeSettingsPane {

	private final boolean D_BUTTON_VERT = false;
	private final String D_LABEL = "Single m/z or Range";
	private final String[] D_BUTTON_EL = new String[] { "Single", "Range" };

	private final String D_MZ = "m/z : ";
	private final String D_MIN_MZ = "Minimum m/z :";
	private final String D_MAX_MZ = "Maximum m/z";

	private final boolean D_MAX_TYPE_VERT = false;
	private final String D_MAX_TYPE_LABEL = "Maximum Intensity Value :";
	private final String[] D_MAX_TYPE_EL = { "Local", "Global" };
	
	private final String D_IMG_WIDTH = "Image Width : ";
	private final String D_IMG_HEIGHT = "Image Height : ";

	private DialogComponent singleRange = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(MZViewerNodeModel.BUTTON_GROUP_ID, "Single"), D_BUTTON_VERT,
			D_LABEL, D_BUTTON_EL);

	private DialogComponent mz = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelDoubleBounded(MZViewerNodeModel.MZ_ID, 0, 0, Integer.MAX_VALUE),
			D_MZ);

	private DialogComponent minMZ = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(MZViewerNodeModel.MZ_MIN_ID, 0, 0, Integer.MAX_VALUE), D_MIN_MZ);

	private DialogComponent maxMZ = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(MZViewerNodeModel.MZ_MAX_ID, 0, 0, Integer.MAX_VALUE), D_MAX_MZ);
	
	private DialogComponent maxType = new DialogComponentButtonGroup(SettingsModelCreate.createSettingsModelString(
			MZViewerNodeModel.MAX_TYPE_ID, "Local"), D_MAX_TYPE_VERT, D_MAX_TYPE_LABEL, D_MAX_TYPE_EL);

	private DialogComponent imgWidth = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(MZViewerNodeModel.IMG_WIDTH_ID, 1, 1, 3000),
			D_IMG_WIDTH);
	
	private DialogComponent imgHeight = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(MZViewerNodeModel.IMG_HEIGHT_ID, 1, 1, 3000),
			D_IMG_HEIGHT);
	
	/**
	 * New pane for configuring the GetMZRange node.
	 */
	protected MZViewerNodeDialog() {

		addDialogComponent(singleRange);

		addDialogComponent(mz);
		
		createNewGroup("Range m/z values");

		addDialogComponent(minMZ);

		addDialogComponent(maxMZ);
		
		closeCurrentGroup();

		addDialogComponent(maxType);
		
		createNewGroup("Image Size");
		
		addDialogComponent(imgWidth);
		
		addDialogComponent(imgHeight);
		
		closeCurrentGroup();
	}
}

