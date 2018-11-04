package msk.util;

import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

public class SettingsModelCreate {

	/**
	 * SettingsModel Creator for String type.
	 * 
	 * @param id
	 *            is the ID of Strings used above
	 * @param defaultValue
	 *            is the default value of the SettingsModelString.
	 * @return SettingsModel for the FileChooser in our dialog.
	 */
	public static SettingsModelString createSettingsModelString(String id, String defaultValue) {
		return new SettingsModelString(id, null);
	}

	/**
	 * SettingsModelDouble creator.
	 *
	 * @param id
	 *            is the String ID as seen above.
	 * @param d
	 *            is the default value
	 * @return the SettingsModelDouble for the min and max m/z
	 */
	public static SettingsModelDouble createSettingsModelDouble(String id, double defaultVal) {
		return new SettingsModelDouble(id, defaultVal);
	}

	/**
	 * SettingsModelInteger creator.
	 * 
	 * @param id
	 *            is the String ID.
	 * @param val
	 *            is the default value
	 * @return the SettingsModelInteger for the given parameters.
	 */
	public static SettingsModelInteger createSettingsModelInteger(String id, int defaultVal) {
		return new SettingsModelInteger(id, defaultVal);
	}

	/**
	 * SettingsModelIntegerBounded creator
	 * 
	 * @param id
	 *            is the String id of the model
	 * @param defaultVal
	 *            is the default value of the model
	 * @param minVal
	 *            is the minimum possible value allowed
	 * @param maxVal
	 *            is the maximum possible value allowed.
	 * @return a SettingsModelIntegerBounded for the given parameters.
	 */
	public static SettingsModelIntegerBounded createSettingsModelIntegerBounded(String id, int defaultVal, int minVal,
			int maxVal) {
		return new SettingsModelIntegerBounded(id, defaultVal, minVal, maxVal);
	}

	/**
	 * Returns a SettingsModelDoubleBounded for the specified parameters.
	 * 
	 * @param id
	 *            the id of the model
	 * @param defaultVal
	 *            the default value
	 * @param minVal
	 *            the minimum bound
	 * @param maxVal
	 *            the max bound
	 * @return the SettingsModel for the specified parameters.
	 */
	public static SettingsModelDoubleBounded createSettingsModelDoubleBounded(String id, double defaultVal,
			double minVal, double maxVal) {
		return new SettingsModelDoubleBounded(id, defaultVal, minVal, maxVal);
	}

	/**
	 * Generic SettingsModelStringArray creator.
	 * 
	 * @param id
	 *            is the string identifier for the model.
	 * @return SettingsModelString with a null default val.
	 */
	public static SettingsModelStringArray createSettingsModelStringArray(String id) {
		return new SettingsModelStringArray(id, null);
	}


}
