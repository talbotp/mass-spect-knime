package msk.util;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.eclipse.draw2d.BorderLayout;

import imzML.ImzML;

/**
 * A View class for the Reader Nodes in KNIME. The view can be used to display
 * important information to the user about the imzML files.
 * 
 * @author Andrew P Talbot
 */
@SuppressWarnings("serial")
public class GenericReaderViewPanel extends JPanel {

	/**
	 * Construct the table to appear in the view of the node.
	 */
	@SuppressWarnings("unused")
	public GenericReaderViewPanel(ImzML imzML, String filename) {

		// Some conditional values need to be done up here as opposed to the
		// table input array.
		String fileType = (imzML.isContinuous()) ? "Continuous" : "Processed";
		int nChannels = imzML.getSpectrum(1, 1).getmzArray().length;
		String nChannelsStr = (imzML.isContinuous()) ? "" + nChannels : "n/a, the data is processed.";
		String minMZ = (imzML.isContinuous()) ? "" + imzML.getSpectrum(1, 1).getmzArray()[0]
				: "n/a, the data is processed.";
		String maxMZ = (imzML.isContinuous()) ? "" + imzML.getSpectrum(1, 1).getmzArray()[nChannels - 1]
				: "n/a, the data is processed.";

		String column[] = { "Name", "Value" };
		String data[][] = { { "File Location : ", filename }, { "Version : ", imzML.getVersion() },
				{ "Data Type : ", fileType }, { "Image Width : ", "" + imzML.getWidth() },
				{ "Image Height : ", "" + imzML.getHeight() }, { "Number of m/z Channels : ", nChannelsStr },
				{ "Minimum m/z : ", minMZ }, { "Maximum m/z : ", maxMZ}};

		JTable jtable = new JTable(data, column);
		JScrollPane sp = new JScrollPane(jtable);

		this.add(new JScrollPane(jtable), BorderLayout.CENTER);
	}

}
