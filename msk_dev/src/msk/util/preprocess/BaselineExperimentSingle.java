package msk.util.preprocess;

import msk.util.ImzMLWrapper;

/**
 * Here we perform basic baseline removal experiment.
 * 
 * @author parker
 *
 */
public class BaselineExperimentSingle {

	private int xPixel;
	private int yPixel;

	public BaselineExperimentSingle(int xPixel, int yPixel) {
		this.xPixel = xPixel;
		this.yPixel = yPixel;

		ImzMLWrapper imzml = new ImzMLWrapper(
				"/Users/parker/work/imzMLPracticeDatasets/s042_continuous-1/S042_Continuous.imzML");
		
		
		
	}

}
