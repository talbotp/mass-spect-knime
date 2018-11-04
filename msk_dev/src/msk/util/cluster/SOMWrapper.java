package msk.util.cluster;

import org.knime.core.data.DataRow;
import org.knime.core.node.BufferedDataTable;

import msk.util.DataManipulator;
import net.sf.javaml.clustering.SOM;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.CosineDistance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.ManhattanDistance;
import net.sf.javaml.distance.NormDistance;

/**
 * Wrapper class for Self ORgranizing Maps in msk.
 * 
 * Dependant on java ml library.
 * 
 * @author Andrew P Talbot
 *
 */
public class SOMWrapper {

	// SOM.Gridtype for input.
	public static final int GRIDTYPE_HEXAGONAL = 0;
	public static final int GRIDTYPE_RECTANGLES = 1;

	// SOM.LearningType for input.
	public static final int LEARNING_EXPONENTIAL = 2;
	public static final int LEARNING_INVERSE = 3;
	public static final int LEARNING_LINEAR = 4;

	// SOM.NeighborhoodFunction for input.
	public static final int NEIGHBORHOOD_GAUSSIAN = 8;
	public static final int NEIGHBORHOOD_STEP = 9;

	// Distance measures for the SOM. CAN ADD MORE HERE IF NEEDED : TODO
	public static final int DISTANCE_METRIC_EUCLIDEAN = 20;
	public static final int DISTANCE_METRIC_COSINE = 21;
	public static final int DISTANCE_METRIC_MANHATTAN = 22;
	public static final int DISTANCE_METRIC_NORM_DIST = 23;

	private SOM som;
	private Dataset dataset;
	private Dataset[] clustered;

	/**
	 * Construct the Java ML SOM object here.
	 * 
	 * @param xdim
	 * @param ydim
	 * @param grid
	 * @param iterations
	 * @param learningRate
	 * @param initialRadius
	 * @param learning
	 * @param nbf
	 * @param dm
	 */
	public SOMWrapper(int xdim, int ydim, int gridType, int iterations, double learningRate, int initialRadius,
			int learningType, int nbhFunction, int distanceMetric) {
		this.som = new SOM(xdim, ydim, getGridType(gridType), iterations, learningRate, initialRadius,
				getLearningType(learningType), getNeighborhoodFunction(nbhFunction),
				getDistanceMeasure(distanceMetric));
	}

	/**
	 * Get a valid GridType.
	 */
	private SOM.GridType getGridType(int gridType) {
		if (gridType == GRIDTYPE_HEXAGONAL)
			return SOM.GridType.HEXAGONAL;
		else if (gridType == GRIDTYPE_RECTANGLES)
			return SOM.GridType.RECTANGLES;
		else
			throw new IllegalArgumentException("That is not a valid GridType input.");
	}

	/**
	 * Get a valid LearningType.
	 */
	private SOM.LearningType getLearningType(int learningType) {
		if (learningType == LEARNING_EXPONENTIAL)
			return SOM.LearningType.EXPONENTIAL;
		else if (learningType == LEARNING_INVERSE)
			return SOM.LearningType.INVERSE;
		else if (learningType == LEARNING_LINEAR)
			return SOM.LearningType.LINEAR;
		else
			throw new IllegalArgumentException("That is not a valid LearningType input.");
	}

	/**
	 * Get a valid Nieghborhood Function
	 */
	private SOM.NeighbourhoodFunction getNeighborhoodFunction(int nhbFunction) {
		if (nhbFunction == NEIGHBORHOOD_GAUSSIAN)
			return SOM.NeighbourhoodFunction.GAUSSIAN;
		else if (nhbFunction == NEIGHBORHOOD_STEP)
			return SOM.NeighbourhoodFunction.STEP;
		else
			throw new IllegalArgumentException("That is not a valid NeighborhoodFunction input.");
	}

	/**
	 * Get a valid DistanceMeasure.
	 */
	private DistanceMeasure getDistanceMeasure(int dist) {
		if (dist == DISTANCE_METRIC_EUCLIDEAN)
			return new EuclideanDistance();
		else if (dist == DISTANCE_METRIC_COSINE)
			return new CosineDistance();
		else if (dist == DISTANCE_METRIC_MANHATTAN)
			return new ManhattanDistance();
		else if (dist == DISTANCE_METRIC_NORM_DIST)
			return new NormDistance();
		else
			throw new IllegalArgumentException("That is not a valid DistanceMeasure input.");
	}

	/**
	 * Perform SOM using the som field object and the Dataset field variables.
	 */
	public void cluster() {
		this.clustered = this.som.cluster(this.dataset);
	}

	/**
	 * Dataset must have been clustered by calling cluster() before calling this
	 * method.
	 * 
	 * @return the Dataset array consisting of the clustered data.
	 */
	public Dataset[] getSOMClusters() {
		return this.clustered;
	}

	/**
	 * Creates and sets the dataset field, adds each row as an instance to the
	 * DataSet.
	 * 
	 * MUST BE CALLED BY THE USER BEFORE WE PERFORM ANY CLUSTERING.
	 * 
	 * @param table
	 *            is the BufferedDat
	 */
	public void setDataSet(BufferedDataTable table) {
		Dataset data = new DefaultDataset();
		for (DataRow row : table) {
			data.add(createInstance(row));
		}
		this.dataset = data;
	}

	/**
	 * Creates an instance from a given DataRow.
	 * 
	 * Creates array from row and adds that data, the class label is the to
	 * string of the key of the row.
	 * 
	 * @param row
	 *            is the row we create an instance of.
	 * @return the instance from the given row.
	 */
	public Instance createInstance(DataRow row) {
		return new DenseInstance(DataManipulator.createRowArray(row), row.getKey().getString());
	}
	
}
