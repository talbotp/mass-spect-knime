package msk.util;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;

import imzML.ImzML;

/**
 * This is a class that is used as a wrapper to handle ImzML data.
 * 
 * @author Andrew P Talbot
 * @version 28/06/2018
 *
 */
public class ImzMLUtil {

	// **************************************************************************** // 
	// **************************************************************************** // 
	// **************************************************************************** // 
	
	//THESE STATIC METHODS ARE ALL STILL IN USE.
	
	// **************************************************************************** // 
	// **************************************************************************** // 
	// **************************************************************************** // 
	

	/**
	 * Returns an ImzML object.
	 * 
	 * NB : USE getImzML(String, NodeLogger) in the actual nodes.
	 * 
	 * @param filename
	 *            is the filname to the ImzML file.
	 * @return the ImzML Object for the filename.
	 */
	public static ImzML getImzML(String filename) {
		return imzMLConverter.ImzMLHandler.parseimzML(filename);
	}

	/**
	 * Creates an ImzML object to be used to parse the data.
	 * 
	 * REQUIRES: imzML and ibd file to be in same directory. Wraps the Logger
	 * and the getImzML(:String):ImzML method in this class
	 * 
	 * @param filename
	 *            is the path to the directory.
	 * @param logger
	 *            is the NodeLogger to throw the error if a file error occurs.
	 *
	 * @return the ImzML file for the given imzML folder
	 */
	public static ImzML getImzML(String filename, NodeLogger logger) {
		try {
			isImzMLFileExtension(filename);
			logger.info("Valid imzML file extension.");
			return getImzML(filename);
		} catch (InvalidSettingsException e) {
			String errorMsg = "That is not an imzML file, please check the file.";
			logger.fatal(errorMsg);
			return null;
		} catch (Exception e) {
			String errorMsg = "There has been an issue with the imzML file."
					+ " Check if the imzML file and ibd file are in the same location";
			logger.fatal(errorMsg);
			return null;
		}
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
	public static void isImzMLFileExtension(String filename) throws InvalidSettingsException {
		boolean isImzML = true;
		if (filename == null) {
			isImzML = false;
		} else {
			int dotIndex = filename.lastIndexOf('.');
			if (filename.substring(dotIndex + 1).equals("imzML")) {
				return;
			} else {
				isImzML = false;
			}
		}

		if (!isImzML) {
			throw new InvalidSettingsException(
					"The input file does not have an imzML file extension, check it is not the .ibd file.");
		}
	}

	/**
	 * Returns the file name of .imzML file (any file extension with one dot at
	 * the end). Without the file extension appended
	 * 
	 * @param filename
	 *            is the full imzML file name
	 * @return the filename without imzML at the end.
	 */
	public static String getFileName(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		return filename.substring(0, dotIndex);
	}

}
