package msk.util.cluster;

/**
 * Trivial class that we create to store the index of a eigenvalue so it can be
 * properly sorted using Collections.
 */
public class EigenvalueIndexPair implements Comparable<EigenvalueIndexPair> {

	public int index;
	public double eigenvalue;

	public EigenvalueIndexPair(int index, double eigenvalue) {
		this.index = index;
		this.eigenvalue = eigenvalue;
	}

	@Override
	public int compareTo(EigenvalueIndexPair other) {
		double diff = this.eigenvalue - other.eigenvalue;
		if (diff == 0)
			return 0;
		return (diff < 0) ? -1 : 1;
	}

	@Override
	public String toString() {
		return eigenvalue + "";
	}

}