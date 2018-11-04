package msk.util;

import java.util.Iterator;

import java.awt.Image;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import imzML.ImzML;
import mzML.Spectrum;

/**
 * This is a wrapper class which handles all things ImzML file related.
 * 
 * Nothing associated with KNIME is used in this class, please use the KNIME
 * version in nodes.
 * 
 * NB : We create a Spectrum Iterator in this class also.
 * 
 * @author Andrew P Talbot
 * @version 11/08/2018
 */
public class ImzMLWrapper implements Iterable<Spectrum> {

	private ImzML imzML;
	private SpectrumIterator specIter;

	/**
	 * Constructor for the ImzML Wrapper. Simply sets the imzML file, if it is
	 * null after setting, then we throw an exception.
	 * 
	 * @param filename
	 *            is the filename of the imzML file
	 * @throws IllegalArgumentException
	 *             when the file was not set correctly.
	 */
	public ImzMLWrapper(String filename) throws IllegalArgumentException {

		setImzML(filename);

		if (imzML == null)
			throw new IllegalArgumentException("Could not create the ImzML object.");
	}

	/**
	 * Create an iterator, allowing for quick and easy access to the spectra in
	 * an imzML file.
	 * 
	 * REQURES : The imzML file must have at least one spectra.
	 */
	@Override
	public Iterator<Spectrum> iterator() {
		this.specIter = new SpectrumIterator(this.imzML);
		return this.specIter;
	}

	/**
	 * Return the current x pixel that the iterator is at.
	 * 
	 * REQUIREMENT : The iterator must not be null.
	 * 
	 * @return the current x pixel of the iterator.
	 */
	public int getCurrentXPixel() {
		return this.specIter.getCurrentXPixel();
	}

	/**
	 * Return the current y pixel that the iterator is at.
	 * 
	 * REQUIREMENT : The iterator must not be null.
	 * 
	 * @return the current y pixel of the iterator.
	 */
	public int getCurrentYPixel() {
		return this.specIter.getCurrentYPixel();
	}

	/**
	 * Class to define an iterator over the spectra of an imzML file.
	 * 
	 * REQUIRES : The imzML file must have at least one spectra.
	 */
	public static class SpectrumIterator implements Iterator<Spectrum> {

		private ImzML imzML;

		private int currentXPixel = 0;
		private int currentYPixel = 1;
		private int nXPixels;
		private int nYPixels;
		private int nPixels;
		private int iteration;

		/**
		 * Constructor for the spectrum iterator.
		 * 
		 * Set the imzML input and the number of pixels of the imzML file.
		 * 
		 * @param imzML
		 *            is the imzML file that we create a spectrum iterator for.
		 */
		protected SpectrumIterator(ImzML imzML) {
			this.imzML = imzML;

			this.nXPixels = imzML.getWidth();
			this.nYPixels = imzML.getHeight();
			this.nPixels = this.nXPixels * this.nYPixels;
			this.iteration = 0;
		}

		/**
		 * Only has next when the pixel is within the bounds of the imzML image
		 * that we have created. The field validPixel is initially set to be
		 * true, as we assume that the imzML file has at least one spectra.
		 */
		@Override
		public boolean hasNext() {
			return this.iteration < this.nPixels;
		}

		/**
		 * We Iterate onto the next spectrum associated with the next pixel.
		 * 
		 * We increment the currentXPixel and currentYPixel fields appropriately
		 * in the nextPixel() method.
		 */
		@Override
		public Spectrum next() {
			nextPixel();
			this.iteration++;
			return this.imzML.getSpectrum(this.currentXPixel, this.currentYPixel);
		}

		/**
		 * We cannot remove spectra from a file so we simply throw an
		 * UnsupportedOperatinException.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot remove a Spectrum from an imzML file.");
		}

		/**
		 * Increment the currentXPixel and currentYPixel fields. Also, if the
		 * pixel values go beyond the bounds of our imzML file, then they are
		 * both set to be one.
		 */
		public void nextPixel() {
			currentXPixel++;
			if (currentXPixel > nXPixels) {
				currentXPixel = 1;
				currentYPixel++;
				if (currentYPixel > nYPixels) {
					currentYPixel = 1;
				}
			}
		}

		/**
		 * 
		 * @return the currentXPixel the iterator is on.
		 */
		public int getCurrentXPixel() {
			return this.currentXPixel;
		}

		/**
		 * 
		 * @return the currentYPixel the iterator is on.
		 */
		public int getCurrentYPixel() {
			return this.currentYPixel;
		}

	}

	public ImzML getImzML() {
		return this.imzML;
	}

	/**
	 * Setter for the ImzML Object.
	 * 
	 * @param filename
	 *            is the filename of the imzML object.
	 */
	public void setImzML(String filename) {
		try {
			isImzMLFileExtension(filename);
			System.out.println("That has a valid imzML file extension");
			this.imzML = makeImzML(filename);
			System.out.println("The imzML file has been successfully set.");
			return;
		} catch (IllegalArgumentException e) {
			String errorMsg = "That is not an imzML file, please check you have "
					+ "entered a file with an imzML extension.";
			System.out.println(errorMsg);
			displayErrorMsg(errorMsg, "icons/knime_icon.png", 100, 100);
			return;
		} catch (NullPointerException e) {
			String errorMsg = "That file doesn't exist in the specified directory.";
			System.out.println(errorMsg);
			displayErrorMsg(errorMsg, "icons/knime_icon.png", 100, 100);
			return;
		} catch (Exception e) {
			String errorMsg = "There has been an issue with the imzML file."
					+ " Check if the imzML file and ibd file are in the same location";
			System.out.println(errorMsg);
			displayErrorMsg(errorMsg, "icons/knime_icon.png", 100, 100);
			return;
		}
	}

	/**
	 * Returns an ImzML object.
	 * 
	 * @param filename
	 *            is the filname to the ImzML file.
	 * @return the ImzML Object for the filename.
	 */
	public static ImzML makeImzML(String filename) throws NullPointerException {
		return imzMLConverter.ImzMLHandler.parseimzML(filename);
	}

	/**
	 * Checks that a file has an imzML file extension
	 * 
	 * USE CASE: for validating in NodeMode.validateSettings() to check the
	 * value of the fileChooser is legtiimate. Yet to be implemented into the
	 * NodeModels of the readers however.
	 * 
	 * @param filename
	 *            is the file that we check
	 * @throws InvalidSettingsEception
	 *             if the file doesn'y have an imzML extension.
	 * @return true if the file has an imzMLExtension.
	 */
	public static void isImzMLFileExtension(String filename) throws IllegalArgumentException {
		if (filename != null) {
			int dotIndex = filename.lastIndexOf('.');
			if (filename.substring(dotIndex + 1).equals("imzML"))
				return;
		}
		throw new IllegalArgumentException(
				"The input file does not have an imzML file extension, check it is not the .ibd file.");
	}

	/**
	 * Returns the file name of .imzML file (any file extension with one dot at
	 * the end). Without the file extension appended
	 * 
	 * REQUIRES : The input must have a . in the string.
	 * 
	 * @param filename
	 *            is the full imzML file name
	 * @return the filename without the file extension.
	 */
	public static String getFileName(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		return filename.substring(0, dotIndex);
	}

	/**
	 * Create a JFrame displaying an error message, saying why the imzML could
	 * not be loaded.
	 * 
	 * @return a simple frame displaying the specified Error message.
	 */
	public static void displayErrorMsg(String errorMsg, String iconPath, int iconWidth, int iconHeight) {
		ImageIcon icon = new ImageIcon(iconPath);
		Image image = icon.getImage(); // transform it
		Image newimg = image.getScaledInstance(iconWidth, iconHeight, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg); // transform it back
		JOptionPane.showMessageDialog(null, errorMsg, "imzML Error", JOptionPane.INFORMATION_MESSAGE, icon);
	}

}
