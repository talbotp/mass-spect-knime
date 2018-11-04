package msk.node.viz.spectrum.jpeg;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "SpectrumJPEG" Node. Create JPEG files for
 * each spectrum and then saves them in a specified location.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class SpectrumJPEGNodeDialog extends DefaultNodeSettingsPane {

	private final String D_FILE_CHOOSER_LABEL = "File Destination :";

	private final String D_ROW_COL_LABEL = "Spectra Row or Columns?";
	private final boolean D_ROW_COL_VERT = false;
	private final String[] D_ROW_COL_EL = { "Row", "Column" };

	// private final String GROUP_SIZE = "JPEG Size";
	private final String D_WIDTH_LABEL = "Width : ";
	private final String D_HEIGHT_LABEL = "Height :";

	private DialogComponent file_chooser = new DialogComponentFileChooser(
			SettingsModelCreate.createSettingsModelString(SpectrumJPEGNodeModel.FILE_CHOOSER_ID, null),
			D_FILE_CHOOSER_LABEL, JFileChooser.SAVE_DIALOG, true);

	private DialogComponent row_col = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SpectrumJPEGNodeModel.ROW_COL_ID, "Row"), D_ROW_COL_VERT,
			D_ROW_COL_LABEL, D_ROW_COL_EL);

	private DialogComponent width_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumJPEGNodeModel.WIDTH_ID, 1100, 1, Integer.MAX_VALUE),
			D_WIDTH_LABEL);

	private DialogComponent height_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SpectrumJPEGNodeModel.HEIGHT_ID, 500, 1, Integer.MAX_VALUE),
			D_HEIGHT_LABEL);

	/**
	 * New pane for configuring the SpectrumJPEG node.
	 */
	protected SpectrumJPEGNodeDialog() {

		addDialogComponent(file_chooser);

		addDialogComponent(row_col);

		addDialogComponent(width_size);

		addDialogComponent(height_size);
	}
}
