package msk.node.viz.spectrum.meanbp;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "MeanBasepeakSpectrum" Node. Node which
 * creates and returns and sketches a mean and a basepeak spectrum.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class MeanBasepeakSpectrumNodeDialog extends DefaultNodeSettingsPane {

	private final String D_FILE_CHOOSER_LABEL = "File Destination :";

	private final String D_TYPE_LABEL = "Spectra Type?";
	private final boolean D_TYPE_VERT = false;
	private final String[] D_TYPE_EL = { "Mean", "Basepeak" };

	private final String D_YES_NO_LABEL = "Save as JPEG??";
	private final boolean D_YES_NO_VERT = false;
	private final String[] D_YES_NO_EL = { "Yes", "No" };

	private final String D_WIDTH_LABEL = "Width : ";
	private final String D_HEIGHT_LABEL = "Height :";

	private DialogComponent file_chooser = new DialogComponentFileChooser(
			SettingsModelCreate.createSettingsModelString(MeanBasepeakSpectrumNodeModel.FILE_CHOOSER_ID, null),
			D_FILE_CHOOSER_LABEL, JFileChooser.SAVE_DIALOG, true);

	private DialogComponent type_chooser = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(MeanBasepeakSpectrumNodeModel.TYPE_ID, "Mean"), D_TYPE_VERT,
			D_TYPE_LABEL, D_TYPE_EL);

	private DialogComponent yes_no = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(MeanBasepeakSpectrumNodeModel.YES_NO_ID, "Yes"),
			D_YES_NO_VERT, D_YES_NO_LABEL, D_YES_NO_EL);

	private DialogComponent width_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(MeanBasepeakSpectrumNodeModel.WIDTH_ID, 1100, 1, Integer.MAX_VALUE),
			D_WIDTH_LABEL);

	private DialogComponent height_size = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(MeanBasepeakSpectrumNodeModel.HEIGHT_ID, 500, 1, Integer.MAX_VALUE),
			D_HEIGHT_LABEL);

	/**
	 * New pane for configuring the MeanBasepeakSpectrum node.
	 */
	protected MeanBasepeakSpectrumNodeDialog() {

		addDialogComponent(type_chooser);

		addDialogComponent(yes_no);

		addDialogComponent(file_chooser);

		addDialogComponent(width_size);

		addDialogComponent(height_size);

	}

}
