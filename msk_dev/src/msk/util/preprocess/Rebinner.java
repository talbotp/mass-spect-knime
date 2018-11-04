package msk.util.preprocess;

/**
 * Utility class stores the logic behind Rebinning.
 * 
 * @author Andrew P Talbot
 * @version 20/07/2018
 *
 */
public class Rebinner {

	/**
	 * This method generates a new m/z axis for a spectrum. Based off of
	 * algorithm 1 in Alan race paper.
	 * 
	 * @param minMZ
	 *            is the minimum m/z to be considered for the bin.
	 * @param maxMZ
	 *            is the new maximum m/z to be considered for the bin.
	 * @param deltaMZ
	 *            is the bin size.
	 */
	public static double[] generateNewAxis(double minMZ, double maxMZ, double deltaMZ) {
		if (deltaMZ <= 0)
			throw new IllegalArgumentException("Please use a valid size for the bins.");

		int nBins = (int) Math.ceil((maxMZ - minMZ) / deltaMZ) + 1;
		double[] newMZAxis = new double[nBins];

		// Set all the values
		for (int i = 0; i < nBins; i++) {
			newMZAxis[i] = minMZ + (i * deltaMZ);
		}

		newMZAxis[nBins - 1] = maxMZ;
		return newMZAxis;
	}

	/**
	 * Returns the new rebinned intensity array after rebinning occurs, using
	 * the specified parameters.
	 * 
	 * REQUIRE : Arrays be of size greater than one && deltaMZ > 0. 
	 * 
	 * Based off of algorithm 2 in Alan Race paper.
	 * 
	 * @param M
	 *            is the old m/z array.
	 * @param S
	 *            is the old intensity array.
	 * @param Mrebin
	 *            is the new m/z array.
	 * @param deltaM
	 *            is the new bin size.
	 * @return the new intensity array from the given parameters.
	 */
	public static double[] generateBinnedIntensityArray(double[] M, double[] S, double[] Mrebin, double deltaM) {
		int j = 0;
		double h = deltaM / 2;

		int length = Mrebin.length;
		double[] Srebin = new double[length];
		double mMin = Mrebin[0];
		double mMax = Mrebin[length - 1];

		// Loop through the new spectrum length
		for (int i = 0; i < S.length; i++) {

			// If too small we dont add it
			if (M[i] < mMin)
				continue;

			// If too big we are done :)
			if (M[i] > mMax)
				break;

			double t1 = M[i] - h;
			double t2 = M[i] + h;

			while (j < length && Mrebin[j] < t1)
				j++;

			if (j < length && Srebin[j] < t2)
				Srebin[j] += S[i];

		}
		return Srebin;
	}

}
