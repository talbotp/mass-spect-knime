package msk.test.junit;

import cern.colt.Arrays;
import msk.util.preprocess.Baseline;

/**
 * Class used for sanity checks in development.
 * 
 * @author parker
 *
 */
public class MainTest {

	public static void main(String[] args) {
		Baseline b = new Baseline(new double[] {1,2,3}, "Tophat", 2);
		
		System.out.println(Arrays.toString(b.getBaselineSubtracted()));
		System.out.println(Arrays.toString(b.getBaseline()));
		
		Baseline b1 = new Baseline(new double[] {4,2,4}, "Tophat", 2);
		
		b1.setBaseline();
		b1.removeBaseline();
		
		System.out.println(Arrays.toString(b1.getBaseline()));
		System.out.println(Arrays.toString(b1.getBaselineSubtracted()));
	}
	
}
