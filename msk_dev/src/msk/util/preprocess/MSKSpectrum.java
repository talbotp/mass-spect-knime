package msk.util.preprocess;

import msk.util.KeyLogic;

/**
 * Trivial class to store a Spectrum and its Key (RowKey string value) together.
 * 
 * @author Andrew P Talbot
 */
public class MSKSpectrum implements Comparable<MSKSpectrum> {

	public KeyLogic key;
	public double[] spectrum;

	/**
	 * Construct a Spectrum
	 * 
	 * @param key is the rowkey value of this spectrum.
	 * @param spectrum is the intensity values of our spectrum.
	 */
	public MSKSpectrum(KeyLogic key, double[] spectrum) {
		this.key = key;
		this.spectrum = spectrum;
	}

	@Override
	public int compareTo(MSKSpectrum other) {
		return this.key.compareTo(other.key);
	}

}
