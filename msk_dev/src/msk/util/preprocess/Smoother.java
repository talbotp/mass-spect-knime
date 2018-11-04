package msk.util.preprocess;

import flanagan.analysis.CurveSmooth;

/**
 * Utility class storing logic for Smoother node.
 * 
 * Contains logic for Movin mean smoothing, triangular moving mean smoothing,
 * and uses a library to have a method for savitzky golay smoothing.
 * 
 * TODO Change to a more OOP approach to this like the baseline node.
 * 
 * @author Andrew P Talbot
 * @version 20/07/2018
 */
public class Smoother {

	public static final int SAV_GOLAY = 0;
	public static final int MOVING_MEAN = 1;
	public static final int TRIANG_MOVING_MEAN = 2;

	private int smoothType;
	private int windowSize;

	/**
	 * Constructor for Smoother Object
	 * 
	 * If an even window size is given, we simply consider it as the next
	 * largest odd number, as we require a symmetric window.
	 */
	public Smoother(int smoothType, int windowSize) {
		setSmoothType(smoothType);
		setWindowSize(windowSize);
	}

	/**
	 * Get the smoothed array.
	 * 
	 * @param mz
	 * @param intensity
	 * @return
	 */
	public double[] getSmoothedArray(double[] mz, double[] intensity) {
		double[] smoothed;
		switch (this.smoothType) {

		case 0:
			smoothed = savitzkyGolaySmooth(mz, intensity, windowSize);
			break;
		case 1:
			smoothed = movingMeanSmooth(intensity, windowSize);
			break;
		case 2:
			smoothed = triangularMovingMean(intensity, windowSize);
			break;
		default:
			smoothed = null;
			break;

		}

		return smoothed;
	}

	/**
	 * Returns the result of Savitzky-Golay smoothing on data. (require an array
	 * of x coordinates and a corresponding array of y coordinates).)
	 * 
	 * REQUIRES sgFilterWidth >= 1.
	 * 
	 * @param mzData
	 *            is the x values considered
	 * @param intensityData
	 *            is the y values to be considered
	 * @param sgFilterWidth
	 *            is the filter width for the Savitzky-Golay.
	 * @return the smoothed data from the above parameters.
	 */
	public static double[] savitzkyGolaySmooth(double[] mzData, double[] intensityData, int sgFilterWidth) {
		CurveSmooth csm = new CurveSmooth(mzData, intensityData);
		csm.savitzkyGolay(sgFilterWidth);
		return csm.getSmoothedValues();
	}

	/**
	 * Moving Mean Smoothing method for simple smoothing of a dataset.
	 * 
	 * Based on Equation 1.5 page 19 of Alan Race PhD thesis.
	 * 
	 * REQUIRES windowSize >= 1.
	 * 
	 * @param data
	 *            is the data to smooth.
	 * @param windowSize
	 *            is the window we calculate the means in.
	 * @return the smoothed array for the given parameters..
	 */
	public static double[] movingMeanSmooth(double[] data, int windowSize) {
		int length = data.length;
		
		if (length <= 0 || windowSize <= 0) 
			throw new IllegalArgumentException("Invalid parameters, data.length >= 1 && windowSize > 1");
		
		// Require new array so we don't affect the left side of the window for
		// the next iterations.
		double[] smoothed = new double[length];

		windowSize = windowSize / 2;

		// Loop through the every data point.
		for (int i = 0; i < length; i++) {

			double sum = 0;
			int counter = 0;
			// Loop through the window
			for (int j = -windowSize; j <= windowSize; j++) {
				// make sure it's a valid value.
				if (i - j >= 0 && i - j < length) {
					sum += data[i - j];
					counter++;
				}
			}
			smoothed[i] = ((double) 1 / counter) * sum;
		}

		return smoothed;
	}

	/**
	 * Triangular Moving Mean Triangular Smoothing method for simple smoothing
	 * of a dataset.
	 * 
	 * Based on Equation 1.5 page 19 of Alan Race PhD thesis.
	 * 
	 * REQUIRES windowSize >= 1.
	 *
	 * 
	 * @param data
	 *            is the data to smooth.
	 * @param windowSize
	 *            is the window we calculate the means in.
	 * @return the smoothed array for the given parameters..
	 */
	public static double[] triangularMovingMean(double[] data, int windowSize) {
		int length = data.length;
		
		if (length <= 0 || windowSize <= 0) 
			throw new IllegalArgumentException("Invalid parameters, data.length >= 1 && windowSize > 1");
		
		// Require new array so we don't affect the left side of the window for
		// the next iterations.
		double[] smoothed = new double[length];

		windowSize = windowSize / 2;

		// Loop through the every data point.
		for (int i = 0; i < length; i++) {

			double sum = 0;
			int counter = 0;
			// Loop through the window
			for (int j = -windowSize; j <= windowSize; j++) {
				// make sure it's a valid value.
				if (i - j >= 0 && i - j < length) {
					int weight = windowSize + 1 - Math.abs(j);
					sum += weight * data[i-j];
					counter += weight;
				}
			}
			
			
			smoothed[i] = ((double) 1 / counter) * sum;
		}

		return smoothed;
	}

	public int getSmoothType() {
		return smoothType;
	}

	/**
	 * Set the integer which defines the smoothing type. One of the three static
	 * variables at the top of this class.
	 * 
	 * @param smoothType
	 *            is 0 if Savitzky-Golay, 1 if moving mean, 2 if triangular
	 *            moving mean.
	 * @throws IllegalArgumentException
	 *             if smoothType is not one of 0, 1, 2.
	 */
	public void setSmoothType(int smoothType) {
		if (smoothType == 0 || smoothType == 1 || smoothType == 2)
			this.smoothType = smoothType;
		else
			throw new IllegalArgumentException(smoothType + "is not a valid smoothing value.");
	}

	public int getWindowSize() {
		return this.windowSize;
	}

	/**
	 * Require an odd window size so we throw an exception if not.
	 * 
	 * @param windowSize
	 *            is the window size for calculating the smoothed array.
	 */
	public void setWindowSize(int windowSize) {
		if (windowSize > 0)
			this.windowSize = windowSize;
		else
			throw new IllegalArgumentException("Please ensure that the window size is positive.");
	}

}
