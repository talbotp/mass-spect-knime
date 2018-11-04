package msk.test.junit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.preprocess.TypeSpectrum;

/**
 * JUnit tests for classes TypeSpectrum, MeanSpectrum and Basepeak Spectrum.
 * 
 * @author Andrew P Talbot
 */
public class MeanBasepeakTest {
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test TypeSpectrum.getTypeSpectrum()
	
	@Test
	public void test_1_1() {
		int type = 4;
		int length = 2;
		
		exception.expect(IllegalArgumentException.class);
		TypeSpectrum.typeSpectrumFactory(type, length);
	}

}
