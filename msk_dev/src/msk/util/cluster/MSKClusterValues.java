package msk.util.cluster;

/**
 * A class for the ClusterViewer, which allows for the Column of Clusters to
 * parse into an array.
 * 
 * @author Andrew P Talbot
 *
 */
public class MSKClusterValues {

	/**
	 * Cluster values are stored in the String form 
	 * 
	 * eg. "cluster_1"
	 * 
	 * @param cluster_x is the String cluster value as we see in KNIME.
	 * @return (int) x
	 */
	public static int parseCluster(String cluster_x) {
		return Integer.parseInt(cluster_x.substring(cluster_x.lastIndexOf('_') + 1));
	}

	/**
	 * Turn the cluster column array in KNIME into an array 
	 * 
	 * @param clusterColumn is the String array of cluster values.
	 * @return the integer array of cluster values.
	 */
	public static double[] parseClusterArray(String[] clusterColumn) {
		double[] clusters = new double[clusterColumn.length];
		int index = 0;
		
		try {
			
			while (index < clusters.length)
				clusters[index] = parseCluster(clusterColumn[index++]);
			
		} catch (Exception e) {
			System.out.println("We failed to parse the cluster values at index " + index + ".");
		}
		return clusters;
	}
	
}
