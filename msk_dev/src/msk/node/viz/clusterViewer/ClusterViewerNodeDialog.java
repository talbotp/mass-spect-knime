package msk.node.viz.clusterViewer;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "ClusterViewer" Node.
 * Node used to visualize clusters in KNIME.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class ClusterViewerNodeDialog extends DefaultNodeSettingsPane {

	private final String D_IMG_WIDTH = "Image width";
	private final String D_IMG_HEIGHT = "Image height";
	
	private DialogComponent imgWidth = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(ClusterViewerNodeModel.IMG_WIDTH_ID, 500, 1, 3000),
			D_IMG_WIDTH);
	
	private DialogComponent imgHeight = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(ClusterViewerNodeModel.IMG_HEIGHT_ID, 500, 1, 3000),
			D_IMG_HEIGHT);
	
    /**
     * New pane for configuring the ClusterViewer node.
     */
    protected ClusterViewerNodeDialog() {

    	addDialogComponent(imgWidth);
    	
    	addDialogComponent(imgHeight);
    	
    }
    
}
