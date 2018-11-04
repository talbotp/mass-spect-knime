package msk.node.post.cluster.som;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "SOM" Node. This node is useful for
 * performing Self orgranizng map clustering in KNIIME.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class SOMNodeDialog extends DefaultNodeSettingsPane {

	// Create the Strings to be displayed on the dialog.
	private final boolean D_GRID_VERT = false;
	private final String D_GRID_LAB = "Grid type";
	private final String[] D_GRID_EL = new String[] { "Hexagonal", "Rectangles" };

	private final boolean D_LEARN_VERT = false;
	private final String D_LEARN_LAB = "Learning Type";
	private final String[] D_LEARN_EL = new String[] { "Exponential", "Inverse", "Linear" };

	private final boolean D_NHB_VERT = false;
	private final String D_NHB_LAB = "Neighborhood Type";
	private final String[] D_NHB_EL = new String[] { "Gaussian", "Step" };

	private final boolean D_DIST_VERT = false;
	private final String D_DIST_LAB = "Distance Measure";
	private final String[] D_DIST_EL = new String[] { "Euclidean", "Manhattan", "Cosine", "Norm" };

	private final String D_LEARN_RATE = "Learning Rate";

	private final String D_INIT_RADIUS = "Initial Radius";

	private final String D_ITER = "Number of Iterations";

	private final String D_XDIM = "Number of x Dimensions";

	private final String D_YDIM = "Number of y Dimensions";

	private DialogComponent gridType = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SOMNodeModel.GRID_TYPE_ID, "Hexagonal"), D_GRID_VERT,
			D_GRID_LAB, D_GRID_EL);
	
	private DialogComponent nhbType = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SOMNodeModel.NHB_TYPE_ID, "Gaussian"), D_NHB_VERT,
			D_NHB_LAB, D_NHB_EL);

	private DialogComponent learnType = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SOMNodeModel.LEARN_TYPE_ID, "Exponential"), D_LEARN_VERT,
			D_LEARN_LAB, D_LEARN_EL);

	private DialogComponent distanceType = new DialogComponentButtonGroup(
			SettingsModelCreate.createSettingsModelString(SOMNodeModel.DIST_TYPE_ID, "Euclidean"), D_DIST_VERT,
			D_DIST_LAB, D_DIST_EL);

	private DialogComponent learnRate = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelDoubleBounded(SOMNodeModel.LEARN_RATE_ID, 0.1, 0, Integer.MAX_VALUE),
			D_LEARN_RATE);

	private DialogComponent radius = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelIntegerBounded(SOMNodeModel.INITIAL_RADIUS_ID, 8, 1, Integer.MAX_VALUE), D_INIT_RADIUS);

	private DialogComponent iterations = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(SOMNodeModel.N_ITERATIONS_ID, 1000, 1, Integer.MAX_VALUE),
			D_ITER);

	private DialogComponent nXDim = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(SOMNodeModel.N_X_DIM_ID, 2, 1, Integer.MAX_VALUE),
			D_XDIM);

	private DialogComponent nYDim = new DialogComponentNumberEdit(
			SettingsModelCreate.createSettingsModelIntegerBounded(SOMNodeModel.N_Y_DIM_ID, 2, 1, Integer.MAX_VALUE),
			D_YDIM);

	/**
	 * New pane for configuring the SOM node.
	 */
	protected SOMNodeDialog() {

		addDialogComponent(gridType);

		addDialogComponent(learnType);

		addDialogComponent(distanceType);
		
		addDialogComponent(nhbType);

		addDialogComponent(learnRate);

		addDialogComponent(radius);

		addDialogComponent(iterations);

		addDialogComponent(nXDim);

		addDialogComponent(nYDim);

	}
}
