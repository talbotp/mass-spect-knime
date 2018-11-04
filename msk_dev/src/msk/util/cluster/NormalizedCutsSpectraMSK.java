package msk.util.cluster;

import java.util.ArrayList;

import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.knime.core.data.DataRow;
import org.knime.core.node.BufferedDataTable;

import msk.util.DataManipulator;
import msk.util.KeyLogic;

/**
 * Here is a class that we use to implement Normalized Cuts for the use of MSI
 * data.
 * 
 * @author Andrew P Talbot
 */
public class NormalizedCutsSpectraMSK extends NormalizedCuts {

	public ArrayList<double[]> spectra;

	public NormalizedCutsSpectraMSK(double sigmaI, double sigmaX, double r, double l, double nCutThreshold, int nCutType,
			int nClusters, double accuracy, boolean useEigenvectors, BufferedDataTable table) {
		super(sigmaI, sigmaX, r, l, nCutThreshold, nCutType, nClusters, accuracy, useEigenvectors);

		// Set the spectra arraylist
		setSpectra(table);
		
		// Get the final key value at bottom of table.
		KeyLogic finalKey = new KeyLogic(DataManipulator.getFinalRowKey(table));
		
		// Set these basic fields that are required for the algo.
		setImgWidth(finalKey.getXPixel());
		setImgHeight(finalKey.getYPixel());
		setnPixels(getImgWidth() * getImgHeight());
		
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

		return (spatialDistance < getR())
				? getFeatureFactor(i, j) * getSpatialFactor(spatialDistance) : 0;
	}

	/**
	 * Set the list of orrays of intensity spectra here.
	 * 
	 * @param table
	 *            is the table of combined m.z and intensity values.
	 */
	public void setSpectra(BufferedDataTable table) {
		this.spectra = new ArrayList<>();
		for (DataRow row : table)
			spectra.add(DataManipulator.createRowArray(row));
	}

}
