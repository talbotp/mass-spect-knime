//package msk.util.cluster;
//
//import org.apache.commons.math3.linear.OpenMapRealMatrix;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.knime.core.data.DataRow;
//import org.knime.core.data.container.CloseableRowIterator;
//import org.knime.core.node.BufferedDataTable;
//
//import msk.util.DataManipulator;
//import msk.util.KeyLogic;
//import msk.util.MathMSK;
//
///**
// * Class to perform Normalized Cuts for Mass Spectrometry Imaging datasets in
// * KNIME.
// * 
// * @author Andrew P Talbot
// * @version 11/08/2018
// */
//public class NormalizedCutsMSK extends NormalizedCuts {
//
//	/**
//	 * {@inheritDoc}
//	 * 
//	 * NB : We wish to not set the BufferedDataTable table as a
//	 * 
//	 * @param table
//	 *            is the BufferedDataTable consisting of the spectra as rows in
//	 *            the data.
//	 */
//	public NormalizedCutsMSK(double sigmaI, double sigmaX, double r, double l, double nCutThreshold, int nCutType,
//			int nClusters, BufferedDataTable table) {
//		super(sigmaI, sigmaX, r, l, nCutThreshold, nCutType, nClusters);
//
//		// We set the value of the number of pixels in our msi data.
//		KeyLogic key = new KeyLogic(DataManipulator.getFinalRowKey(table));
//		int key_x = key.getXPixel();
//		int key_y = key.getYPixel();
//
//		setImgWidth(key_x);
//		setImgHeight(key_y);
//		setnPixels(key_x * key_y);
//
//		// setW()
//		setSimilarityMatrixMSK(table);
//	}
//
//	/**
//	 * Set the similarity matrix W.
//	 * 
//	 * @param table
//	 *            is the BufferedDataTable containing mass spectra on the rows
//	 *            of the table.
//	 */
//	public void setSimilarityMatrixMSK(BufferedDataTable table) {
//
//		int npixels = getnPixels();
//		RealMatrix W = new OpenMapRealMatrix(npixels, npixels);
//		for (int i = 0; i < npixels; i++) {
//			
//			CloseableRowIterator iter = table.iterator();
//			DataRow baseRow = null;
//			int counter = 0;
//			// Loop through to the ith value.
//			while (iter.hasNext() && counter <= i) {
//				baseRow = iter.next();
//				counter++;
//			}
//
//			// Now we have the baserow, get the base intensity.
//			double[] baseArr = DataManipulator.createRowArray(baseRow);
//			// Get the pixel coordinates for point baseArr.
//			int base_x = i % getImgWidth();
//			int base_y = i / getImgWidth();
//
//			/*
//			 * Now we loop through the rest of the values and set them in the
//			 * next loop. Iterator should be same length as the npixels, so no
//			 * hasNext() should always be true in this loop, and the final
//			 * iteration should make it false.
//			 */
//			for (; counter < npixels; counter++) {
//
//				// Get the pixel coordinates for point j.
//				int other_x = counter % getImgWidth();
//				int other_y = counter / getImgWidth();
//
//				double spatialDistance = getSpatialWeight(new double[] { base_x, base_y },
//						new double[] { other_x, other_y });
//
//				// If the distance is greater, it will only get greater, thus we
//				// can break the loop
//				// Perhaps this should be continue; ??
//				if (spatialDistance >= getR())
//					break;
//
//				// Call iter.next() here.
//				double[] row = DataManipulator.createRowArray(iter.next());
//
//				double weight = getFeatureFactorMSK(baseArr, row) * getSpatialFactor(spatialDistance);
//				W.setEntry(i, counter, weight);
//				W.setEntry(counter, i, weight);
//			}
//
//			// Finally at end of the loop we close.
//			iter.close();
//		}
//		
//		setW(W);
//	}
//
//	/**
//	 * Returns the feature factor as seen in NormCuts paper.
//	 * 
//	 * We calculate the Euclidean distance and then the factor for ncuts.
//	 * 
//	 * @param spec1
//	 *            is the intensity array for spectrum 1.
//	 * @param spec2
//	 *            is the intensity array for spectrum 2.
//	 * @return the feature factor for the two arrays..
//	 */
//	public double getFeatureFactorMSK(double[] spec1, double[] spec2) {
//		return Math.exp(- Math.pow(MathMSK.l2NormDiff(spec1, spec2), 2) / getSigmaI());
//	}
//
//	/*********************************************************************/
//	/*
//	 * These are the abstract methods, due to KNIME being weird, we have to
//	 * implement our own methods so these are unsupported.
//	 */
//	/*********************************************************************/
//
//	/**
//	 * We do not support this operation because we cannot set the
//	 * BufferedDataTable as a field variable according to the KNIME noding
//	 * guidelines.
//	 */
//	@Override
//	public void setSimilarityMatrix() {
//		throw new UnsupportedOperationException("Please use setSimilarityMatrixMSK()");
//	}
//
//	@Override
//	public double getFeatureFactor(int i, int j) {
//		throw new UnsupportedOperationException("Please use getFeatureFactorMSK()");
//	}
//
//	@Override
//	public double getWeight(int i, int j) {
//		throw new UnsupportedOperationException("Please use getFeatureFactorMSK()");
//	}
//
//}
