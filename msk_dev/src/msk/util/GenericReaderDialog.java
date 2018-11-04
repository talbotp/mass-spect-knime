package msk.util;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * This is a generic dialog that is used for imzML reader nodes. So that we
 * don't have to repeat loads of code in other nodes.
 * 
 * @author Andrew P Talbot
 * @version 10/07/2018
 */
public class GenericReaderDialog extends DefaultNodeSettingsPane {

	// Create the Strings to be displayed on the dialog.
	private final String D_FILE_CHOOSER = "imzML File :";
	private final String D_SPECTRA = "Spectra Per Iteration :";
	private final SettingsModelString sms;
	private final SettingsModelIntegerBounded smsIB;
	private DialogComponent imzMLFileChooser;
	private DialogComponent spectraPerIteration;

	/**
	 * Generic Dialog File Chooser for imzML readers.
	 */
	public GenericReaderDialog(SettingsModelString sms, SettingsModelIntegerBounded smsIB) {
		this.sms = sms;
		this.smsIB = smsIB;
		setDialogComponentSpectra();
		
		setDialogComponentFileChooser();
		addDialogComponent(imzMLFileChooser);
		
		// Fix the look when theres no spectra per iter.
		if (spectraPerIteration != null) {
			addDialogComponent(spectraPerIteration);
		} else {
			closeCurrentGroup();
		}
	}

	public void setDialogComponentFileChooser() {
		imzMLFileChooser = new DialogComponentFileChooser(sms, D_FILE_CHOOSER);
	}
	
	public void setDialogComponentSpectra() {
		if (this.smsIB != null)
			spectraPerIteration = new DialogComponentNumberEdit(smsIB, D_SPECTRA);
	}

}
