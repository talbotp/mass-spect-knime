package msk.node.viz.spectrum.single;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "SpectrumView" Node. This node is used to
 * plot and view a single spectrum.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class SpectrumViewNodeDialog extends DefaultNodeSettingsPane {

	private final String D_X_PIXEL_LABEL = "x Pixel : ";
	private final String D_Y_PIXEL_LABEL = "y Pixel :";

	private final String D_YES_NO_LABEL = "Save the jpeg?";
	private final boolean D_YES_NO_VERT = false;
	private final String[] D_YES_NO_EL = { "Yes", "No" };

	private final String D_WIDTH_LABEL = "Width : ";
	private final String D_HEIGHT_LABEL = "Height :";

	private final String D_FILE_CHOOSER_LABEL = "File Destination :";

	private DialogComponent x_pixel = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumViewNodeModel.X_PIXEL_ID, 1, 1, Integer.MAX_VALUE),
			D_X_PIXEL_LABEL);

	private DialogComponent y_pixel = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumViewNodeModel.Y_PIXEL_ID, 1, 1, Integer.MAX_VALUE),
			D_Y_PIXEL_LABEL);

	private DialogComponent save_yes_no = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SpectrumViewNodeModel.YES_NO_ID, null), D_YES_NO_VERT,
			D_YES_NO_LABEL, D_YES_NO_EL);

	private DialogComponent file_chooser = new DialogComponentFileChooser(
			SettingsModelCreate.createSettingsModelString(SpectrumViewNodeModel.FILE_CHOOSER_ID, null),
			D_FILE_CHOOSER_LABEL, JFileChooser.SAVE_DIALOG, true);

	private DialogComponent width_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumViewNodeModel.WIDTH_ID, 1100, 1, Integer.MAX_VALUE),
			D_WIDTH_LABEL);

	private DialogComponent height_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumViewNodeModel.HEIGHT_ID, 500, 1, Integer.MAX_VALUE),
			D_HEIGHT_LABEL);

	/**
	 * Construct the Panel
	 */
	protected SpectrumViewNodeDialog() {

		addDialogComponent(x_pixel);

		addDialogComponent(y_pixel);

		addDialogComponent(save_yes_no);

		addDialogComponent(width_size);

		addDialogComponent(height_size);

		addDialogComponent(file_chooser);
	}

}
