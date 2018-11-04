package msk.util;

/**
 * Math methods Utility class.
 * 
 * @author Andrew P Talbot
 *
 */
public class MathMSK {

	/**
	 * Modified binary search algorithm which finds the index of the closest
	 * value to an input element in an array.
	 * 
	 * @param a
	 *            is the array
	 * @param value
	 *            is the element we search for the closest element to.
	 * @return the index of the closest element to value in a.
	 */
	public static int binarySearchClosest(double[] a, double value) {

		if (a.length < 1)
			throw new IllegalArgumentException("Please use an array of length greater than 0.");
		
		if (value < a[0]) {
			return 0;
		}
		if (value > a[a.length - 1]) {
			return a.length - 1;
		}

		int lo = 0;
		int hi = a.length - 1;

		while (lo <= hi) {
			int mid = (hi + lo) / 2;

			if (value < a[mid]) {
				hi = mid - 1;
			} else if (value > a[mid]) {
				lo = mid + 1;
			} else {
				return mid;
			}
		}
		// lo == hi + 1
		return (a[lo] - value) < (value - a[hi]) ? lo : hi;
	}

	/**
	 * Remove all negative values in an array and replace them with a 0.
	 * 
	 * @param arr
	 *            is the array we remove negatives from.
	 * @return arr with no negative.s
	 */
	public static double[] removeNegatives(double[] arr) {
		int length = arr.length;
		for (int i = 0; i < length; i++) {
			if (arr[i] < 0)
				arr[i] = 0;
		}
		return arr;
	}

	/**
	 * Calculates the l2 norm for a given double array
	 * 
	 * @param p
	 *            is the norm we caluclate
	 * @param spec
	 *            is the spectrum we calculate the norm of
	 * @return the p norm for the spec array.
	 */
	public static double l2Norm(double[] spec) {

		if (spec.length < 1)
			throw new IllegalArgumentException("The array requires a length of at least 1.");

		double sum = 0;
		for (double val : spec) {
			sum += Math.pow(Math.abs(val), 2);
		}
		return Math.sqrt(sum);
	}

	/**
	 * REQUIRE : spec1.length == spec2.length
	 * 
	 * @param spec1
	 *            is the first spectrum
	 * @param spec2
	 *            is the second spectrum
	 * @return l2Norm(spec1 - spec2).
	 */
	public static double l2NormDiff(double[] spec1, double[] spec2) {
		int length = spec1.length;
		double[] arr = new double[spec1.length];
		for (int i = 0; i < length; ++i) {
			arr[i] = spec1[i] - spec2[i];
		}
		return l2Norm(arr);
	}

	/**
	 * Method for merging two arrays.
	 */
	public static int[] merge(int[] a, int[] b) {

		int length = a.length + b.length;

		int[] v = new int[length];

		for (int i = 0; i < length; i++)
			v[i] = (i < a.length) ? a[i] : b[i - a.length];

		return v;
	}

}
