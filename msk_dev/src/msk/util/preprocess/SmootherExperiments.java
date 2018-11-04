package msk.util.preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import msk.util.ImzMLWrapper;
import msk.util.viz.SpectrumImager;
import mzML.Spectrum;

/**
 * Here are the expetiments on our smoothing methods.
 * 
 * @author Andrew P Talbot
 */
public class SmootherExperiments {
	
	public ArrayList<Double> results;
	public Smoother smooth;
	public ImzMLWrapper imzml;
	Random r = new Random();
	public String smoothType;

	/**
	 * Make Smoother exepriment.
	 */
	public SmootherExperiments(Smoother smoother, String smoothType) {
		imzml = new ImzMLWrapper(
				"/Users/parker/work/imzMLPracticeDatasets/s042_continuous-1/S042_Continuous.imzML");
		smooth = smoother;
		this.smoothType = smoothType;
	}
	
	public double[] addNoiseToArray(double[] spec) {
		double[] tmp = new double[spec.length];
		for (int i = 0; i < spec.length; i++) {
			double mean = spec[i];
			double noise = r.nextGaussian() * Math.sqrt(mean) + mean;
			tmp[i] = spec[i] + noise;
		}
		return tmp;
	}

	public void doExperiment() {
		results = new ArrayList<>();
		
		Iterator<Spectrum> iter = imzml.iterator();
		Spectrum spec = null;
		int n = 0;
		
		double[] intensity = null;
		double[] mz = null;
		double[] smoothed = null;
		double[] noisy = null;
		
		while (iter.hasNext()) {
			
			spec = iter.next();
			mz = spec.getmzArray();
			intensity = spec.getIntensityArray();
			
			noisy = addNoiseToArray(intensity);
			
			smoothed = smooth.getSmoothedArray(mz, noisy);
			
			results.add(msk.util.MathMSK.l2NormDiff(intensity, smoothed));
		
		
			if (n == 150) {
				SpectrumImager.getSpectrumPanelStatic(smoothType + " : " + smooth.getWindowSize(), spec.getmzArray(),
						new ArrayList<>(Arrays.asList(intensity, noisy, smoothed)), 800, 400, true,
						"/Users/parker/work/report_generated_images/smooth",
						new String[] { "Raw", "Noisy", "Smoothed" });
				
			}

			n++;
		
		}
		getResults();
	}
	
	public double getResults() {
		double sum = 0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		for (Double d : results) {
			sum += d;
			if (d < min)
				min = d;
			
			if (d > max)
				max = d;
		}
		
		System.out.println(sum / results.size());
		System.out.println(min);
		System.out.println(max);
		
		return sum / results.size();
	}

	/*
	 * Run the experiments by running this class.
	 */
	public static void main(String[] args) {
		Smoother smoother = new Smoother(Smoother.TRIANG_MOVING_MEAN, 3);
		
		SmootherExperiments exper = new SmootherExperiments(smoother, "Triangular Moving Mean");
		
		exper.doExperiment();
		
		smoother.setWindowSize(7);
		exper.doExperiment();
		
		smoother.setWindowSize(11);
		exper.doExperiment();
	}

}
