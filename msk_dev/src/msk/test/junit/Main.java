package msk.test.junit;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		double[] mzVals = {1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d};
		double minMZ = 2;
		double maxMZ = 4;
		
		// Get an array of values to keep.
		ArrayList<Integer> toKeep = new ArrayList<>();
		for (int i = 0; i < mzVals.length; i++) {
			double tmp = mzVals[i];
			if (tmp < minMZ)
				continue;
			else if (tmp > maxMZ)
				break;
			toKeep.add(i);
		}
		
		System.out.println(toKeep);
		
	}

}
