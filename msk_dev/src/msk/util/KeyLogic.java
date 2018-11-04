package msk.util;

import org.knime.core.data.RowKey;

/**
 * Utility class to standardize the Row Keys used in our KNIME nodes.
 * 
 * toString() Should output a key in the form "(x,y)" where x == xPixel and y ==
 * yPixel.
 * 
 * @author Andrew P Talbot
 * @version 20/07/2018
 */
public class KeyLogic implements Comparable<KeyLogic> {

	private int xPixel;
	private int yPixel;

	/**
	 * Constructor when creating a KeyString object, to make a RowKey.
	 * 
	 * @param x
	 *            is the x pixel value.
	 * @param y
	 *            is the y pixel value.
	 */
	public KeyLogic(int x, int y) {
		xPixel = x;
		yPixel = y;
	}

	/**
	 * Constructor for when we take in a RowKey
	 * 
	 * @param k
	 *            is the RowKey.
	 */
	public KeyLogic(RowKey k) {
		String key = k.getString();
		xPixel = Integer.parseInt(getXPixelString(key));
		yPixel = Integer.parseInt(getYPixelString(key));
	}

	/**
	 * Constructor for taking in a key in string form, for efficiency in some
	 * cases.
	 * 
	 * @param keyString
	 *            is the string value of the key.
	 */
	public KeyLogic(String keyString) {
		try {
			xPixel = Integer.parseInt(getXPixelString(keyString));
			yPixel = Integer.parseInt(getYPixelString(keyString));
		} catch (NumberFormatException e) {
			System.out.println("That is not a valid Key.");
			throw new IllegalArgumentException("Please insert a valid key.");
		}
	}

	public int getXPixel() {
		return xPixel;
	}

	public int getYPixel() {
		return yPixel;
	}

	/**
	 * General Key value
	 */
	public String toString() {
		return getPixelKey(xPixel, yPixel);
	}

	/**
	 * Returns a row key for a String value for a RowKey/Column name for a given
	 * x value and y value for pixels
	 * 
	 * @param x
	 *            is the pixel location in the width.
	 * @param y
	 *            is the pixel location in the height
	 * @return the identifier for the given location of x and y.
	 */
	public static String getPixelKey(int x, int y) {
		return "(" + x + "," + y + ")";
	}

	/**
	 * Returns the x coordinate in the standard row key that we use.
	 * 
	 * @param key
	 *            is the String value of the key.
	 */
	public static String getXPixelString(String key) {
		return key.substring(1, key.indexOf(','));
	}

	/**
	 * Returns the y value of the pixel key.
	 * 
	 * @param key
	 *            is the key we return the y for.
	 * @return the y value.
	 */
	public static String getYPixelString(String key) {
		int commaIndex = key.lastIndexOf(',');
		int bracketIndex = key.lastIndexOf(')');
		return key.substring(commaIndex + 1, bracketIndex);
	}

	/**
	 * Compare two different KeyLogic objects.
	 */
	@Override
	public int compareTo(KeyLogic other) {
		int diff = this.xPixel - other.xPixel;
		if (diff == 0)
			diff = this.yPixel - other.yPixel;
		return diff;
	}

}
