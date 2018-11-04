package msk.node.pre.spec.rebin;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "SpectrumRebinner" Node. This node is used
 * for rebinning mass spectrometry datasets.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class RebinnerNodeDialog extends DefaultNodeSettingsPane {

	private final String D_BIN_SIZE_LABEL = "Bin Width :";
	private final String D_MIN_MZ_LABEL = "Minimum m/z :";
	private final String D_MAX_MZ_LABEL = "Maximum m/z :";
	
	private DialogComponent minMZNumberEdit = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(RebinnerNodeModel.MIN_MZ_SIZE_ID, 0, 0, Double.MAX_VALUE),
			D_MIN_MZ_LABEL);

	private DialogComponent maxMZNumberEdit = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(RebinnerNodeModel.MAX_MZ_SIZE_ID, 0, 0, Double.MAX_VALUE),
			D_MAX_MZ_LABEL);


	private DialogComponent binSizeNumberEdit = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(RebinnerNodeModel.BIN_SIZE_ID, 1, 0.01, Double.MAX_VALUE),
			D_BIN_SIZE_LABEL);

	/**
	 * New pane for configuring the SpectrumRebinner node.
	 */
	protected RebinnerNodeDialog() {
		
		addDialogComponent(minMZNumberEdit);
		
		addDialogComponent(maxMZNumberEdit);
		
		addDialogComponent(binSizeNumberEdit);
		
	}

}
