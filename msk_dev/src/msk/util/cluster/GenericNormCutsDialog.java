package msk.util.cluster;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * Generic Dialog to be used in the Normalized Cuts nodes in KNIME.
 * 
 * @author Andrew P Talbot
 */
public class GenericNormCutsDialog extends DefaultNodeSettingsPane {

	private final String D_IMG_FILE_PATH = "Image File : ";
	private final String D_PARTITION_TYPE = "Partitioning method : ";
	private final boolean D_PARTITION_VERT = false;
	private final String[] D_PARTITION_EL = { "Zero", "Median", "NCut" };
	private final String D_SIGMAI = "Sigma I : ";
	private final String D_SIGMAX = "Sigma X : ";
	private final String D_DISTANCE_FACTOR = "Distance Measure r : ";
	private final String D_THRESHOLD = "Cut Threshold : ";
	private final String D_GAP_SIZE_L = "Gap size for partition : ";
	private final String D_MAX_CLUSTERS = "Max Number of Clusters : ";

	// Lanczos values go here
	private final String D_ACCURACY = "Lanczos Accuracy : ";
	private final String D_COMPARISON_LABEL = "Compare eigenvecotors or values :";
	private final boolean D_COMPARISON_VERT = false;
	private final String[] D_COMPARISON_EL = { "Eigenvectors", "Eigenvalues" };

	private DialogComponent img_file, sigma_i, sigma_x, distance_r, partition_type, gap_l, threshold_, max_cluster,
			lanczos_accuracy, lanczos_comparison;;

	public GenericNormCutsDialog(SettingsModelString fileChooser, SettingsModelDoubleBounded smsSigmaI,
			SettingsModelDoubleBounded smsSigmaX, SettingsModelDoubleBounded smsR, SettingsModelString smsPType,
			SettingsModelDoubleBounded smsL, SettingsModelDoubleBounded smsThreshold,
			SettingsModelIntegerBounded smsMaxCluster, SettingsModelDoubleBounded lanczosAccuracy,
			SettingsModelString lanczosComparison) {

		// Set the DialogComponents first. NB: For the spectra node, we dont
		// need file chooser hence we block this one out.
		if (fileChooser != null)
			this.img_file = new DialogComponentFileChooser(fileChooser, D_IMG_FILE_PATH);

		this.sigma_i = new DialogComponentNumberEdit(smsSigmaI, D_SIGMAI);
		this.sigma_x = new DialogComponentNumberEdit(smsSigmaX, D_SIGMAX);
		this.distance_r = new DialogComponentNumberEdit(smsR, D_DISTANCE_FACTOR);
		this.partition_type = new DialogComponentButtonGroup(smsPType, D_PARTITION_VERT, D_PARTITION_TYPE,
				D_PARTITION_EL);
		this.gap_l = new DialogComponentNumberEdit(smsL, D_GAP_SIZE_L);
		this.threshold_ = new DialogComponentNumberEdit(smsThreshold, D_THRESHOLD);
		this.max_cluster = new DialogComponentNumberEdit(smsMaxCluster, D_MAX_CLUSTERS);

		// Lanczos values go here
		this.lanczos_accuracy = new DialogComponentNumberEdit(lanczosAccuracy, D_ACCURACY);
		this.lanczos_comparison = new DialogComponentButtonGroup(lanczosComparison, D_COMPARISON_VERT,
				D_COMPARISON_LABEL, D_COMPARISON_EL);
		;

		if (img_file != null)
			addDialogComponent(img_file);
		addDialogComponent(sigma_i);
		addDialogComponent(sigma_x);
		addDialogComponent(distance_r);

		addDialogComponent(partition_type);

		closeCurrentGroup();

		createNewGroup("NCut Partition Variables : ");
		addDialogComponent(gap_l);
		addDialogComponent(threshold_);
		closeCurrentGroup();

		createNewGroup("Zero/Median Partition Variables : ");
		addDialogComponent(max_cluster);
		closeCurrentGroup();

		createNewGroup("Lanczos Values : ");
		addDialogComponent(lanczos_accuracy);
		addDialogComponent(lanczos_comparison);
		closeCurrentGroup();
	}

}
