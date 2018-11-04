package msk.util.preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Utility class storing logic for Baseline Subtraction.
 * 
 * @author Andrew P Talbot
 * @version 20/07/2018
 *
 */
public class Baseline {

	private static final LinkedList<String> validBaseTypes = new LinkedList<String>(
			Arrays.asList("Minimum", "Median", "Tophat", "Optimal Tophat"));

	// Types of BaseLine removal
	public static final int MINIMUM = 0;
	public static final int MEDIAN = 1;
	public static final int TOPHAT = 2;
	public static final int OPTIMAL_TOPHAT = 3;

	private double[] spec;
	private int baseType;
	private int windowSize;
	private double[] baseline;

	/**
	 * Constructor for Baseline object.
	 * 
	 * @param spec
	 *            is the spectrum to remove the baseline of.
	 * @param baseType
	 *            is the method of baseline removal we use.
	 */
	public Baseline(double[] spec, String baseType, int windowSize) {

		setBaselineType(baseType);
		setWindowSize(windowSize);
		setSpectrum(spec);

		// Baseline MUST BE SET IN subtractBaseline() to ensure no errors in
		// calculatuon.

	}

	/**
	 * Baseline Constructor so that we can create the baseline at the start of a
	 * loop.
	 */
	public Baseline(String baseType, int windowSize) {
		setBaselineType(baseType);
		setWindowSize(windowSize);
	}

	/**
	 * 
	 * @return spectrum array.
	 */
	public double[] getSpectrum() {
		return this.spec;
	}
	
	/**
	 * @return windowSize
	 */
	public int getWindowSize() {
		return this.windowSize;
	}
	
	/**
	 * 
	 * @return the baselineType
	 */
	public int getType() { 
		return this.baseType;
	}

	/**
	 * This is the method used to calculate the baseline, and then remove it
	 * from the spectrum.
	 */
	public double[] getBaselineSubtracted() {
		setBaseline();

		removeBaseline();

		spec = msk.util.MathMSK.removeNegatives(spec);

		return spec;
	}

	/**
	 * 
	 * @return the baseline.
	 */
	public double[] getBaseline() {
		return this.baseline;
	}

	/**
	 * Setter for the spectrum object.
	 * 
	 * @param spec
	 *            is the new spectrum.
	 */
	public void setSpectrum(double[] spec) {
		this.spec = spec;
	}

	/**
	 * Checks if the baseline subtraction type is supported.
	 * 
	 * @param baseType
	 *            is the method of baseline subtraction.
	 */
	public void setBaselineType(String baseType) {
		if (validBaseTypes.contains(baseType)) {
			if (baseType.equals("Minimum"))
				this.baseType = Baseline.MINIMUM;
			else if (baseType.equals("Median"))
				this.baseType = Baseline.MEDIAN;
			else if (baseType.equals("Tophat"))
				this.baseType = Baseline.TOPHAT;
			else // "Optimal Tophat"
				this.baseType = Baseline.OPTIMAL_TOPHAT;
		} else
			throw new IllegalArgumentException(baseType + " is not a valid method of baseline subtraction.");
	}

	/**
	 * Removes the baseline from the spec field.
	 * 
	 * @return the intensity - the baseline array
	 */
	public void removeBaseline() {
		int length = spec.length;
		for (int i = 0; i < length; i++) {
			spec[i] -= baseline[i];
		}
	}

	/**
	 * Set the window size of the baseline removal.
	 * 
	 * @param windowSize
	 *            is the windowsize to perform the baseline removal.
	 */
	public void setWindowSize(int windowSize) {
		if (windowSize <= 0)
			throw new IllegalArgumentException("Window must be greater than 0.");
		if (windowSize % 2 == 0)
			windowSize++; // Need odd windowsize
			
		this.windowSize = windowSize;
	}

	/**
	 * Sets the baseline field variable according to the baseType field.
	 */
	public void setBaseline() {
		double[] baseline;
		if (baseType == Baseline.MINIMUM) {
			baseline = getMinimumBaseline(spec.clone(), windowSize);
		} else if (baseType == Baseline.MEDIAN) {
			baseline = getMedianBaseline(spec.clone(), windowSize);
		} else if (baseType == Baseline.TOPHAT) {
			baseline = getTophatBaseline();
		} else { // baseType == Baseline.OPTIMAL_TOPHAT
			baseline = getOptimalTophatBaseline(spec.clone(), windowSize);
		}

		this.baseline = baseline;
	}

	/**
	 * Method to calculate the minimumn baseline.
	 * 
	 * See Alan race Thesis.
	 * 
	 * @return the minimum baseline for this.spec
	 */
	public static double[] getMinimumBaseline(double[] spec, int windowSize) {
		int lengthBaseline = spec.length;
		
		if (lengthBaseline == 1)
			return spec;
		
		double[] baseline = new double[lengthBaseline];
		
		int side = windowSize / 2;
		
		// Loop through all elements in the intensity array.
		for (int i = 0; i < lengthBaseline; i++) {
			
			double min = spec[i];
			
			// We search for smaller elements in the window.
			for (int j = -side; j <= side; j++) {				
				int current = i - j;
				if (current >= 0 && current < lengthBaseline) {
					if (spec[current] < min)
						min = spec[current];
				}
			}
			
			baseline[i] = min;
		}
		return baseline;
	}

	/**
	 * 
	 * @return the median baseline for this.spec
	 */
	public static double[] getMedianBaseline(double[] spec, int windowSize) {
		int lengthBaseline = spec.length;
		
		if (lengthBaseline == 1)
			return spec;

		double[] baseline = new double[lengthBaseline];
		
		int side = windowSize / 2;
		
		for (int i = 0; i < lengthBaseline; i++) {
			
			ArrayList<Double> toConsider = new ArrayList<>();
			
			for (int j = -side; j <= side; j++) {
				int current = i - j;
				if (current >= 0 && current < lengthBaseline)
					toConsider.add(spec[current]);
			}
			
			Double[] consider = toConsider.toArray(new Double[0]);
			Arrays.sort(consider);
			double median;
			if (consider.length % 2 == 0)
				median = ((double) consider[consider.length / 2] + (double) consider[consider.length / 2 - 1])
						/ 2;
			else
				median = (double) consider[consider.length / 2];
			
			baseline[i] = median;
		}
		
		return baseline;
	}

	/**
	 * Morphological operator Tophat, returns the opening of an array with a
	 * structuring element.
	 * 
	 * REQUIRES : structEl > 1
	 * 
	 * @param intArr
	 *            array we return the Tophat of.
	 * @param structEl
	 *            is the structure / window for the Tophat operator.
	 * @return the Tophat of the given parameters.
	 */
	public double[] getTophatBaseline() {
		return getOpening(spec.clone(), windowSize);
	}

	/**
	 * Returns an optimal baseline.
	 * 
	 * As presented by ROSANNA PEREZ-PUEYO, MARIA JOSE SONEIRA and SERGIO
	 * RUIZ-MORENO in Morphology-Based Automated Baseline Removal for Raman
	 * Spectra of Artistic Pigments.
	 * 
	 * @return the optimal Tophat baseline.
	 */
	public static double[] getOptimalTophatBaseline(double[] spec, int windowSize) {
		int length = spec.length;

		// Get opening
		double[] opening = getOpening(spec, windowSize);

		double[] dilatedOpening = getDilation(opening, windowSize);
		double[] erodedOpening = getErosion(opening, windowSize);

		double[] average = new double[length];

		for (int i = 0; i < length; i++) {
			average[i] = (dilatedOpening[i] + erodedOpening[i]) / 2;
		}

		// Calculate the minimum of the opening and the average
		for (int i = 0; i < length; i++) {
			// We return the average to make it easier.

			if (opening[i] < average[i])
				average[i] = opening[i];
		}

		return average;
	}

	/*
	 * _________________________________________________________________________
	 * 
	 * FROM HERE ONWARDS ARE JUST STATIC METHODS FOR THE MORPHOLOGICAL
	 * OPERATORS.
	 */

	/**
	 * Morphological operator Dilation.
	 * 
	 * REQUIRES : structEl > 1
	 * 
	 * @param intArr
	 *            is the array to calculate the dilation of
	 * @param structEl
	 *            is the structuring element of the dilation
	 * @return the dilated arraua
	 */
	public static double[] getDilation(double[] intArr, int structEl) {
		int lengthBaseline = intArr.length;
		
		if (lengthBaseline == 1)
			return intArr;
		
		double[] baseline = new double[lengthBaseline];
		
		int side = structEl / 2;
		
		// Loop through all elements in the intensity array.
		for (int i = 0; i < lengthBaseline; i++) {
			
			double max = intArr[i];
			
			// We search for smaller elements in the window.
			for (int j = -side; j <= side; j++) {				
				int current = i - j;
				if (current >= 0 && current < lengthBaseline) {
					if (intArr[current] > max)
						max = intArr[current];
				}
			}
			
			baseline[i] = max;
		}
		return baseline;
	}

	/**
	 * Morphological operator Erosion
	 * 
	 * REQUIRES : structEl > 1
	 * 
	 * @param intArr
	 *            array to calculate the erosion for.
	 * @param structEl
	 *            is the structuring element / window size to use.
	 * @return the erosion using the given parameters.
	 */
	public static double[] getErosion(double[] intArr, int structEl) {
		int lengthBaseline = intArr.length;
		
		if (lengthBaseline == 1)
			return intArr;
		
		double[] baseline = new double[lengthBaseline];
		
		int side = structEl / 2;
		
		// Loop through all elements in the intensity array.
		for (int i = 0; i < lengthBaseline; i++) {
			
			double min = intArr[i];
			
			// We search for smaller elements in the window.
			for (int j = -side; j <= side; j++) {				
				int current = i - j;
				if (current >= 0 && current < lengthBaseline) {
					if (intArr[current] < min)
						min = intArr[current];
				}
			}
			
			baseline[i] = min;
		}
		return baseline;
	}

	/**
	 * Morphological operator Opening. (Dilation of the Erosion).
	 * 
	 * REQUIRES : structEl > 1
	 * 
	 * @param intArr
	 *            array to calculate the opening for
	 * @param structEl
	 *            is structuring element / window size.
	 * @return the opening applied on the fiven parameters.
	 */
	public static double[] getOpening(double[] intArr, int structEl) {
		return getDilation(getErosion(intArr, structEl), structEl);
	}

}

//public static double[] OLDgetMinimumBaseline(double[] spec, int windowSize) {
//int lengthBaseline = spec.length;
//double[] baseline = new double[lengthBaseline];
//
//int lengthWindow = 2 * windowSize + 1;
//
//for (int i = 0; i < lengthBaseline; i++) {
//
//	double[] toConsider = new double[lengthWindow];
//	// Set the array.
//	int index = 0;
//	for (int j = i - windowSize; j < i + windowSize + 1; j++) {
//		if (j >= 0 && j < lengthBaseline)
//			toConsider[index] = spec[j];
//		else
//			toConsider[index] = -1;
//		index++;
//	}
//
//	Arrays.sort(toConsider);
//	for (int k = 0; k < lengthWindow; k++) {
//		if (toConsider[k] != -1) {
//			baseline[i] = toConsider[k];
//			break;
//		}
//	}
//}
//return baseline;

///**
// * 
// * @return the median baseline for this.spec
// */
//public static double[] OLDgetMedianBaseline(double[] spec, int windowSize) {
//	int lengthBaseline = spec.length;
//	
//	if (lengthBaseline == 1)
//		return spec;
//	
//	double[] baseline = new double[lengthBaseline];
//
//	int lengthWindow = 2 * windowSize + 1;
//
//	for (int i = 0; i < lengthBaseline; i++) {
//
//		double[] toConsider = new double[lengthWindow];
//		// Set the array.
//		int index = 0;
//		for (int j = i - windowSize; j < i + windowSize + 1; j++) {
//			if (j >= 0 && j < lengthBaseline)
//				toConsider[index] = spec[j];
//			else
//				toConsider[index] = -1;
//			index++;
//		}
//
//		Arrays.sort(toConsider);
//
//		// Calculate the median.
//		double median;
//		if (toConsider.length % 2 == 0)
//			median = ((double) toConsider[toConsider.length / 2] + (double) toConsider[toConsider.length / 2 - 1])
//					/ 2;
//		else
//			median = (double) toConsider[toConsider.length / 2];
//
//		baseline[i] = median;
//	}
//	return baseline;
//}
