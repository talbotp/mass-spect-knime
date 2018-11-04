package msk.util.preprocess;

/**
 * Class that we use to calculate a mean spectrum.
 * 
 * @author Andrew P Talbot
 */
public class MeanSpectrum implements TypeSpectrum {

	private double[] updateSpectrum;
	private int nAdded;

	/**
	 * Constructor for the MeanSpectrum object calculator.
	 * 
	 * @param length
	 *            is the length of the spectra.
	 */
	public MeanSpectrum(int length) {
		updateSpectrum = new double[length];
		nAdded = 0;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void update(double[] spectrum) {
		for (int i = 0; i < spectrum.length; i++)
			updateSpectrum[i] += spectrum[i];
		
		nAdded++;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public double[] getSpecialSpectrum() {
		double[] meanSpec = new double[updateSpectrum.length];

		for (int i = 0; i < meanSpec.length; i++)
			meanSpec[i] = updateSpectrum[i] / nAdded;

		return meanSpec;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == this)
			return true;
		
		if (!(o instanceof MeanSpectrum))
				return false;
		
		MeanSpectrum ms = (MeanSpectrum) o;
		
		if (this.updateSpectrum.equals(ms.updateSpectrum) && this.nAdded == ms.nAdded)
			return true;
		else
			return false;
	}

}
