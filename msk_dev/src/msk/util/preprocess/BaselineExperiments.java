package msk.util.preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiFunction;

import msk.util.ImzMLWrapper;
import msk.util.viz.SpectrumImager;
import mzML.Spectrum;

/**
 * Here are the experiments used in the report where we perform some baseline
 * experiments.
 * 
 * @author parker
 *
 */
public class BaselineExperiments {

	// BASELINE ADDER : Constant shift function
	public static final BiFunction<Double, Double, Double> f_CONSTANT_SHIFT = (x, shift) -> {
		return x + shift;
	};
	
	// BASELINE ADDER : LINEAR
	public static final BiFunction<Double, Double, Double> f_LINEAR = (x, mz) -> {
		double val = x + (-13 / 5d) * (mz) + 600;
		return val >= 0 ? val : x;
	};
	
	public static final BiFunction<Double, Double, Double> f_POLYNOMIAL = (x, mz) -> {
		double val = x - 0.1 * Math.pow((mz - 235),2) + 7;
		return val >= 0 ? val : x;
	};
	
	public static final BiFunction<Double, Double, Double> f_LOGARITHMIC = (x, mz) -> {
		double val = Math.log(- mz + 245);;
		return mz < 244 ? x + val : x;
	};

	// METRIC : Euclidean Norm
	public static final BiFunction<double[], double[], Double> m_EUCLIDEAN_METRIC = (spec1, spec2) -> {
		return msk.util.MathMSK.l2NormDiff(spec1, spec2);
	};

	private Baseline baseline;
	private ImzMLWrapper imzml;
	private ArrayList<Double> results;
	private BiFunction<Double, Double, Double> baselineAdder;
	private BiFunction<double[], double[], Double> metric;
	private String baselineAddedType;
	private String typeRemoval;
	private int Window;

	/**
	 * Construct an experiment.
	 * 
	 * @param baselineAdder
	 *            is the function we use to add a baseline.
	 */
	public BaselineExperiments(Baseline baseline, ImzMLWrapper imzML, BiFunction<Double, Double, Double> baselineAdder,
			BiFunction<double[], double[], Double> metric, String typeRemoval, String baselineAddedType, int Window) {
		this.baseline = baseline;
		this.imzml = imzML;
		this.results = new ArrayList<>();
		this.baselineAdder = baselineAdder;
		this.metric = metric;
		this.baselineAddedType = baselineAddedType;
		this.typeRemoval = typeRemoval;
		this.Window = Window;
	}

	/**
	 * @return the mean value in list results.
	 */
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

	public void doExperiment() {
		results = new ArrayList<>();

		Iterator<Spectrum> iter = imzml.iterator();

		Spectrum spec = null;
		int n = 0;
		while (iter.hasNext()) {
			spec = iter.next();
			
			double[] mz = spec.getmzArray();

			double[] raw = spec.getIntensityArray();
			double[] added = new double[raw.length];

			for (int i = 0; i < added.length; i++) {
				added[i] = baselineAdder.apply(raw[i], mz[i]);
			}

			baseline.setSpectrum(added.clone());
			double[] subtracted = baseline.getBaselineSubtracted();

			double resultMetric = metric.apply(raw, subtracted);
			results.add(resultMetric);

			if (n == 150) {
				
				SpectrumImager.getSpectrumPanelStatic(typeRemoval + " : " + Window + " : " + baselineAddedType , spec.getmzArray(),
						new ArrayList<>(Arrays.asList(added, subtracted , raw)), 800, 400, true,
						"/Users/parker/work/report_generated_images/baseline",
						new String[] { "Baseline Added", "Baseline Subtracted", "Raw" });
			}

			n++;
		}

		getResults();
	}

	public static void main(String[] args) {
		// Make baseline
		//String typeBaseline = "Minimum";
		//String typeBaseline = "Median";
		//String typeBaseline = "Tophat";
		String typeBaseline = "Optimal Tophat";
		int windowSize = 3;
		Baseline b = new Baseline(typeBaseline, windowSize);

		// Make ImzMLWrapper
		String imzmlPath = "/Users/parker/work/imzMLPracticeDatasets/s042_continuous-1/S042_Continuous.imzML";
		ImzMLWrapper imzml = new ImzMLWrapper(imzmlPath);

		// Make experiment.
		BaselineExperiments exper = new BaselineExperiments(b, imzml, BaselineExperiments.f_LOGARITHMIC,
				BaselineExperiments.m_EUCLIDEAN_METRIC, typeBaseline, "Logarithmic", windowSize);
		
		exper.doExperiment();
		
		exper.Window = 7;
		exper.baseline.setWindowSize(7);
		exper.doExperiment();
		
		exper.Window = 11;
		exper.baseline.setWindowSize(11);
		exper.doExperiment();
	}

}
