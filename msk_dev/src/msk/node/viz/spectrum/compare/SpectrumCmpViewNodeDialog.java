package msk.node.viz.spectrum.compare;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentString;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "SpectrumCmpView" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class SpectrumCmpViewNodeDialog extends DefaultNodeSettingsPane {

	private final String D_PIXELS_LABEL = "Pixels";

	private final String D_YES_NO_LABEL = "Save the jpeg?";
	private final boolean D_YES_NO_VERT = false;
	private final String[] D_YES_NO_EL = { "Yes", "No" };
	protected static final String WIDTH_ID = "width_num_edit_id";
	protected static final String HEIGHT_ID = "height_num_edit_id";

	// private final String GROUP_SIZE = "JPEG Size";
	private final String D_WIDTH_LABEL = "Width : ";
	private final String D_HEIGHT_LABEL = "Height :";

	private final String D_FILE_CHOOSER_LABEL = "File Destination :";

	private DialogComponent pixels = new DialogComponentString(
			SettingsModelCreate.createSettingsModelString(SpectrumCmpViewNodeModel.PIXELS_ID, null), D_PIXELS_LABEL);

	private DialogComponent save_yes_no = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SpectrumCmpViewNodeModel.YES_NO_ID, "Yes"), D_YES_NO_VERT,
			D_YES_NO_LABEL, D_YES_NO_EL);

	private DialogComponent file_chooser = new DialogComponentFileChooser(
			SettingsModelCreate.createSettingsModelString(SpectrumCmpViewNodeModel.FILE_CHOOSER_ID, null),
			D_FILE_CHOOSER_LABEL, JFileChooser.SAVE_DIALOG, true);

	private DialogComponent width_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumCmpViewNodeModel.WIDTH_ID, 1100, 1, Integer.MAX_VALUE),
			D_WIDTH_LABEL);

	private DialogComponent height_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumCmpViewNodeModel.HEIGHT_ID, 500, 1, Integer.MAX_VALUE),
			D_HEIGHT_LABEL);

	/**
	 * Construct the Panel
	 */
	protected SpectrumCmpViewNodeDialog() {

		addDialogComponent(pixels);

		addDialogComponent(save_yes_no);
		
		addDialogComponent(width_size);
		
		addDialogComponent(height_size);

		addDialogComponent(file_chooser);

	}

}
