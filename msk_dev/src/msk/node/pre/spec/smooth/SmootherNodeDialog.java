package msk.node.pre.spec.smooth;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "SavitzkyGolaySmoother" Node. This node is to
 * be used for smoothing a set of rows or a set of columns using Savitzky-Golay
 * smoothing, using a filter width specified by the user in the dialog.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 * @version 08/07/2018
 */
public class SmootherNodeDialog extends DefaultNodeSettingsPane {

	private static final String D_TYPE_LABEL = "Smoothing Method";
	private static final boolean D_TYPE_VERT = true;
	private static final String[] D_TYPE_EL = { "Savitzky-Golay", "Moving Mean", "Triangular Moving Mean" };

	private static final String D_FILTER_WIDTH_LABEL = "Filter Width / Window Size";

	private DialogComponent typeButtonGroup = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SmootherNodeModel.TYPE_ID, null), D_TYPE_VERT, D_TYPE_LABEL,
			D_TYPE_EL);

	private DialogComponent filterWidthNumberEdit = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SmootherNodeModel.FILTER_WIDTH_ID, 2, 2, Integer.MAX_VALUE),
			D_FILTER_WIDTH_LABEL);

	/**
	 * New pane for configuring the SavitzkyGolaySmoother node.
	 */
	protected SmootherNodeDialog() {

		addDialogComponent(typeButtonGroup);

		addDialogComponent(filterWidthNumberEdit);
	}
	
}
