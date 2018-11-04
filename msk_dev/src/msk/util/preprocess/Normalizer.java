package msk.util.preprocess;

import java.util.Arrays;
import java.util.LinkedList;

import org.knime.core.node.InvalidSettingsException;

import msk.util.MathMSK;

/**
 * Utility class storing logic for Normalization node.
 * 
 * For Euclidean Normalization, we require the spectrum array to be of length
 * greater than or equal to 1. Otherwise, we can handle the situation, by
 * returning an empty array.
 * 
 * @author Andrew P Talbot
 * @version 20/07/2018
 *
 */
public class Normalizer extends RunnablePreprocessingTask<Normalizer> {

	private static final LinkedList<String> validNormTypes = new LinkedList<String>(
			Arrays.asList("TIC", "Median", "Euclidean Norm"));

	private double[] spec;
	private String normType;
	private double normCoeff;

	/**
	 * Creates a Spectrum Normalizer object.
	 * 
	 * Instance Invariant : normType == "TIC" || normType == "Median" ||
	 * normType == "Euclidean Norm"
	 * 
	 * @param spec
	 *            is the spectrum to normalize, typically the intensity array.
	 * @param normType
	 *            is the type of normalization
	 */
	public Normalizer(double[] spec, String normType) {
		super();
		this.spec = spec;
		this.setNormType(normType);
	}

	/**
	 * Constructor for when we wish to concurrently normalize many spectra at
	 * once.
	 * 
	 * @param threadPool
	 *            is the ThreadPool containing out threads.
	 */
	public Normalizer(PreprocessingThreadPool<Normalizer> threadPool, String type) {
		super(threadPool);
		this.setNormType(type);
	}

	/**
	 * Empty constructor, not recommended for use, as can likely get
	 * NullPointerExceptions if all values are nor properly set.
	 */
	public Normalizer() {

	}

	/**
	 * Spectrum getter
	 * 
	 * @return the spectrum array.
	 */
	public double[] getSpectrum() {
		return spec;
	}

	/**
	 * Getter for the normalization type.
	 * 
	 * @return the normalization type.
	 */
	public String getNormType() {
		return normType;
	}

	/**
	 * Getter for normalizaiton.
	 * 
	 * @return the normalization coefficient
	 */
	public double getNormalizationCoefficient() {
		return normCoeff;
	}

	/**
	 * Setter for the spectrum
	 */
	public void setSpectrum(double[] spec) {
		this.spec = spec;
	}

	/**
	 * Setter for the type of normalization.
	 * 
	 * @param normType
	 *            is the new type of normalization.
	 * @throws IllegalArgumentException
	 *             if an invalid value has been passed.
	 */
	public void setNormType(String normType) throws IllegalArgumentException {
		if (validNormTypes.contains(normType))
			this.normType = normType;
		else
			throw new IllegalArgumentException("That is an invalid value of the field normType : " + normType + ".");
	}

	/**
	 * This sets the Normalization Coefficient.
	 * 
	 * NB : Is private so users cannot illegally set the normalization
	 * coefficient.
	 */
	private void setNormalizationCoefficient() {
		if (normType.equals("TIC"))
			setTICCoeff();
		else if (normType.equals("Median"))
			setMedianCoeff();
		else // Euclidean Norm
			setEuclideanNormCoeff();
	}

	/**
	 * Normalize the spectrum.
	 * 
	 * @return the normalizer spectrum for its given fields.
	 * @throws InvalidSettingsException
	 *             if the instance invariant is broken.
	 */
	public void normalizeSpectrum() {
		/*
		 * Must set the normalization coefficient before normalizing.
		 */
		setNormalizationCoefficient();

		normalize();
	}

	/**
	 * Returns a Normalized Array
	 * 
	 * If ticCoeff == 0, then we don't alter the intArr at all.
	 * 
	 * @param arr
	 *            is the intensity array to be normalized
	 * @param ticCoeff
	 *            is the TIC coefficient of normalization
	 * @return the normalized array by TIC
	 */
	private void normalize() {
		if (normCoeff == 0)
			return;

		int length = spec.length;
		for (int i = 0; i < length; i++) {
			spec[i] /= normCoeff;
		}

	}

	/**
	 * Returns the TIC Coefficient for TIC Normalization See Race Thesis page 22
	 * 
	 * @param arr
	 *            is the array to calculate TIC for
	 * @return the TIC coefficient.
	 */
	private void setTICCoeff() {
		double sum = 0;
		for (double val : spec) {
			sum += Math.abs(val);
		}
		normCoeff = sum;
	}

	/**
	 * Returns the Median coefficient for the spectrum.
	 * 
	 * @return the median of the spectrum..
	 */
	private void setMedianCoeff() {
		// Can make more efficient using selectSort(length / 2)
		double[] tmp = spec.clone();

		// Handle empty array case.
		if (tmp.length <= 0) {
			normCoeff = 0;
			return;
		}

		Arrays.sort(tmp);

		if (tmp.length % 2 == 0)
			normCoeff = ((double) tmp[tmp.length / 2] + (double) tmp[tmp.length / 2 - 1]) / 2;
		else
			normCoeff = (double) tmp[tmp.length / 2];

		// Set the normCoeff as the median.
	}

	/**
	 * Returns the coefficient for the Euclidean norm
	 * 
	 * REQUIRES : Arrays of at least length 1.
	 * 
	 * @param intArr
	 *            is the array to calculate the coefficient of
	 * @return the Euclidean norm coefficient.
	 */
	private void setEuclideanNormCoeff() {
		normCoeff = MathMSK.l2Norm(spec);
		//
		// double sum = 0;
		// for (double val : spec) {
		// sum += Math.pow(Math.abs(val), 2);
		// }
		// normCoeff = Math.sqrt(sum);
		// Set it to be the Euclidean Norm of the spectrum.
	}

	/**********************************************************************/
	// Below is all concurrent code for this preprocessing class
	/**********************************************************************/

	/**
	 * {@inheritcDoc}
	 */
	@Override
	public synchronized void process() {
		MSKSpectrum msk_spec = null;
	    while((msk_spec = threadPool.toProcess.poll()) != null) {
	    	
	        // Normalizer the spectrum
	        this.setSpectrum(msk_spec.spectrum);
	       
	        this.normalizeSpectrum();
	      
	        threadPool.processedIntensity.put(msk_spec.key, this.getSpectrum());
	    }
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Normalizer getConcurrentInstance(PreprocessingThreadPool<Normalizer> threadPool, String type) {
		return new Normalizer(threadPool, type);
	}

}
