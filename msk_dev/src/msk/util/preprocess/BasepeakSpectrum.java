package msk.util.preprocess;

/**
 * Class that we use to calculate a basepeak spectrum.
 * 
 * @author Andrew P Talbot
 */
public class BasepeakSpectrum implements TypeSpectrum {

	private double[] basepeakSpectrum;
	
	/**
	 * Construct a BasepeakSpectrum Object.
	 * 
	 * @param length is the length of the spectra.
	 */
	public BasepeakSpectrum(int length) {
		basepeakSpectrum = new double[length];
	}
	
	@Override
	public void update(double[] spectrum) {
		for (int i = 0; i < spectrum.length; i++)
			if (spectrum[i] > basepeakSpectrum[i])
				basepeakSpectrum[i] = spectrum[i];
	}

	@Override
	public double[] getSpecialSpectrum() {
		return basepeakSpectrum;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == this)
			return true;
		
		if (!(o instanceof BasepeakSpectrum))
				return false;
		
		BasepeakSpectrum bs = (BasepeakSpectrum) o;
		
		if (this.basepeakSpectrum == bs.basepeakSpectrum)
			return true;
		else
			return false;
	}

}
