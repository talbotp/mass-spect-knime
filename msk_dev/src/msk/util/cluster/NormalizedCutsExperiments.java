package msk.util.cluster;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import msk.util.ImzMLWrapper;
import mzML.Spectrum;

/**
 * Normalized Cuts implementation which doesn't require the use of a
 * BufferedDataTable for experiments not in Knime.
 * 
 * @author Andrew P Talbot
 *
 */
public class NormalizedCutsExperiments extends NormalizedCuts {

	public ArrayList<double[]> spectra;

	public NormalizedCutsExperiments(double sigmaI, double sigmaX, double r, double l, double nCutThreshold,
			int nCutType, int nClusters, double accuracy, boolean useEigenvectors, ImzMLWrapper imzml) {
		super(sigmaI, sigmaX, r, l, nCutThreshold, nCutType, nClusters, accuracy, useEigenvectors);

		// Set the spectra arraylist
		setSpectra(imzml);
		setSimilarityMatrix();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSimilarityMatrix() {
		int nPixels = getnPixels();

		RealMatrix W = new OpenMapRealMatrix(nPixels, nPixels);

		// Loop through the array to set the symmetric matrix W.
		for (int i = 0; i < nPixels; i++) {
			for (int j = i; j < nPixels; j++) {

				double w = getWeight(i, j);
				W.setEntry(i, j, w);
				W.setEntry(j, i, w);

			}
		}

		setW(W);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getFeatureFactor(int i, int j) {
		double[] arr_i = spectra.get(i);
		double[] arr_j = spectra.get(j);

		return Math.exp(-msk.util.MathMSK.l2NormDiff(arr_i, arr_j) / getSigmaI());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getWeight(int i, int j) {
		// Get the pixel coordinates for point i.
		int i_x = i % getImgWidth();
		int i_y = i / getImgWidth();

		// Get the pixel coordinates for point j.
		int j_x = j % getImgWidth();
		int j_y = j / getImgWidth();

		// Work out the spatial value X(i) - X(j).
		double spatialDistance = getSpatialWeight(new double[] { i_x, i_y }, new double[] { j_x, j_y });

		return (spatialDistance < getR()) ? getFeatureFactor(i, j) * getSpatialFactor(spatialDistance) : 0;
	}

	/**
	 * Set the list of orrays of intensity spectra here.
	 * 
	 * @param table
	 *            is the table of combined m.z and intensity values.
	 */
	public void setSpectra(ImzMLWrapper imzml) {
		this.spectra = new ArrayList<>();
		Iterator<Spectrum> iter = imzml.iterator();
		Spectrum spec = null;
		while (iter.hasNext()) {
			spec = iter.next();
			spectra.add(spec.getIntensityArray());
		}

		// Final spectra to get key.
		// Set these basic fields that are required for the algo.
		setImgWidth(imzml.getCurrentXPixel());
		setImgHeight(imzml.getCurrentYPixel());
		setnPixels(getImgWidth() * getImgHeight());
	}

	public static void main(String[] args) {
		double sigmaI = 15;
		double sigmaX = 16;
		double r = 2.2;
		double accuracy = 0.0001;
		boolean useEigenvectors = true;
		double l = 0.02;
		double nCutThreshold = 0.05;
		int nCutType = PARTITION_BY_NCUT;
		int nClusters = 3;
		
		String imzmlPath = "/Users/parker/work/imzMLPracticeDatasets/s042_continuous-1/S042_Continuous.imzML";
		ImzMLWrapper imzml = new ImzMLWrapper(imzmlPath);
		
		NormalizedCutsExperiments nc = new NormalizedCutsExperiments(sigmaI, sigmaX, r, l, nCutThreshold, nCutType, nClusters,
				accuracy, useEigenvectors, imzml);
		nc.clusterAndSetW();
		
	}

}
