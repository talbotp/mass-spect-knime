package msk.util.preprocess;

/**
 * Interface for the classes MeanSpectrum and BasepeakSpectrum.
 * 
 * @author Andrew P Talbot
 */
public interface TypeSpectrum {

	public static final int MEAN = 0;
	public static final int BASEPEAK = 1;
	
	public static final String MEAN_STR = "Mean";
	public static final String BASEPEAK_STR = "Basepeak";

	/**
	 * Create a spectrum creator, either one of MeanSpectrum or BasepeakSpectrum
	 * 
	 * @param type
	 *            is 0 or 1.
	 * @param length
	 *            is the length of the arrays of our spectrum.
	 * @return a MeanSpectrum object if type == 0, a BasepeakSpectrum if type ==
	 *         1
	 */
	public static TypeSpectrum typeSpectrumFactory(int type, int length) {
		switch (type) {

		case MEAN:
			return new MeanSpectrum(length);
		case BASEPEAK:
			return new BasepeakSpectrum(length);
		default:
			throw new IllegalArgumentException(type + " is not a valid type of spectrum to calculate.");
		}
	}

	/**
	 * Update the spectrum accordingly with the new spectrum.
	 * 
	 * @param spectrum
	 *            is the spectrum that we use to update our current spectrum.
	 */
	public void update(double[] spectrum);

	/**
	 * Get the special spectrum that we have created. Modify it in any way we
	 * need to.
	 * 
	 * @return the special spectrum for this class.
	 */
	public double[] getSpecialSpectrum();

}
