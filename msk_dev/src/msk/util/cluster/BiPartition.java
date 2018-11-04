package msk.util.cluster;

import java.util.Comparator;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Trivial Class to store a matrix with its original indices Mapping.
 * 
 * We implement Comparator as we wish to sort an array of BiPartition and this
 * is NOT a natural ordering, as we sort so that the biggest will be first.
 * 
 * @author Andrew P Talbot
 *
 */
public class BiPartition implements Comparator<BiPartition> {

	public RealMatrix W;
	public Map<Integer, Integer> partition;

	public BiPartition(RealMatrix W, Map<Integer, Integer> partition) {
		this.W = W;
		this.partition = partition;
	}
	
	public BiPartition() {
		
	}

	@Override
	public int compare(BiPartition o1, BiPartition o2) {
		return -(o1.partition.size() - o2.partition.size());
	}

}
