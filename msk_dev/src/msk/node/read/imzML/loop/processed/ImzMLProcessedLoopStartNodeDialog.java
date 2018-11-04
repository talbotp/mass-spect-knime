package msk.node.read.imzML.loop.processed;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import msk.util.SettingsModelCreate;

/**
 * <code>NodeDialog</code> for the "ImzMLProcessedLoopStart" Node.
 * Node which is used to read in processed imzML datasets.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andrew P Talbot
 */
public class ImzMLProcessedLoopStartNodeDialog extends DefaultNodeSettingsPane {

	private final String D_FILE_CHOOSER = "imzML File :";
	private final String D_SPECTRA = "Spectra Per Iteration :";
	
	private SettingsModelIntegerBounded smsIB = SettingsModelCreate.createSettingsModelIntegerBounded(
			ImzMLProcessedLoopStartNodeModel.N_SPECTRA_ID, 1, 1, Integer.MAX_VALUE);
	private SettingsModelString sms = SettingsModelCreate
			.createSettingsModelString(ImzMLProcessedLoopStartNodeModel.FILE_CHOOSER_ID, null);
	
	private DialogComponent imzMLFileChooser;
	private DialogComponent spectraPerIteration;
	
	private final String D_BIN_SIZE_LABEL = "Bin Width :";
	private final String D_MIN_MZ_LABEL = "Minimum m/z :";
	private final String D_MAX_MZ_LABEL = "Maximum m/z :";
	
	// Rebin components.
	private DialogComponent minMZNumberEdit = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(ImzMLProcessedLoopStartNodeModel.MIN_MZ_SIZE_ID, 0, 0, Double.MAX_VALUE),
			D_MIN_MZ_LABEL);
	private DialogComponent maxMZNumberEdit = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(ImzMLProcessedLoopStartNodeModel.MAX_MZ_SIZE_ID, 0, 0, Double.MAX_VALUE),
			D_MAX_MZ_LABEL);
	private DialogComponent binSizeNumberEdit = new DialogComponentNumberEdit(SettingsModelCreate
			.createSettingsModelDoubleBounded(ImzMLProcessedLoopStartNodeModel.BIN_SIZE_ID, 1, 0.01, Double.MAX_VALUE),
			D_BIN_SIZE_LABEL);
	
    /**
     * New pane for configuring the ImzMLProcessedLoopStart node.
     */
    protected ImzMLProcessedLoopStartNodeDialog() {
    	
    	// Set fields
    	setDialogComponentFileChooser();
    	setDialogComponentSpectra();

    	// Add normal components
		addDialogComponent(imzMLFileChooser);
		addDialogComponent(spectraPerIteration);
		
		// Add rebin components.
		addDialogComponent(minMZNumberEdit);
		addDialogComponent(maxMZNumberEdit);
		addDialogComponent(binSizeNumberEdit);

    }
    
	public void setDialogComponentFileChooser() {
		imzMLFileChooser = new DialogComponentFileChooser(sms, D_FILE_CHOOSER);
	}
	
	public void setDialogComponentSpectra() {
		if (this.smsIB != null)
			spectraPerIteration = new DialogComponentNumberEdit(smsIB, D_SPECTRA);
	}
	
}

