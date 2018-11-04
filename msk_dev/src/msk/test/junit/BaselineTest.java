package msk.test.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cern.colt.Arrays;
import msk.util.preprocess.Baseline;

/**
 * Test Cases for msk.util.preprocess.Baseline
 * 
 * @author Andrew P Talbot
 *
 */
public class BaselineTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test Constructor, Getters, Setters.

	@Test
	public void test_1_1() {
		double[] spec = {};
		String baseType = "NotAValidType";
		int windowSize = 5;

		exception.expect(IllegalArgumentException.class);
		@SuppressWarnings("unused")
		Baseline b = new Baseline(spec, baseType, windowSize);
	}

	@Test
	public void test_1_2() {
		double[] spec = {};
		String baseType = "Minimum";
		int windowSize = -1;

		exception.expect(IllegalArgumentException.class);
		@SuppressWarnings("unused")
		Baseline b = new Baseline(spec, baseType, windowSize);
	}

	@Test
	public void test_1_3() {
		double[] spec = {};
		String baseType = "Minimum";
		int windowSize = 5;

		Baseline b = new Baseline(spec, baseType, windowSize);

		double[] expectedSpec = {};
		double[] actualSpec = b.getSpectrum();
		Assert.assertArrayEquals(expectedSpec, actualSpec, 0);

		b.setSpectrum(new double[] { 1, 2 });
		expectedSpec = new double[] { 1, 2 };
		actualSpec = b.getSpectrum();
		Assert.assertArrayEquals(expectedSpec, actualSpec, 0);

		int expectedType = Baseline.MINIMUM;
		int actualType = b.getType();
		assertEquals(expectedType, actualType);

		b.setBaselineType("Median");
		expectedType = Baseline.MEDIAN;
		actualType = b.getType();
		assertEquals(expectedType, actualType);

		b.setBaselineType("Tophat");
		expectedType = Baseline.TOPHAT;
		actualType = b.getType();
		assertEquals(expectedType, actualType);

		b.setBaselineType("Optimal Tophat");
		expectedType = Baseline.OPTIMAL_TOPHAT;
		actualType = b.getType();
		assertEquals(expectedType, actualType);

		int expectedSize = 5;
		int actualSize = b.getWindowSize();
		assertEquals(expectedSize, actualSize);

		b.setWindowSize(7);
		expectedSize = 7;
		actualSize = b.getWindowSize();
		assertEquals(expectedSize, actualSize);
	}

	// Test Minimum Baseline Correction.

	@Test
	public void test_2_1() {
		double[] spec = {};
		String baseType = "Minimum";
		int windowSize = 1;

		Baseline b = new Baseline(spec, baseType, windowSize);

		double[] expectedSub = {};
		double[] actualSub = b.getBaselineSubtracted();
		Assert.assertArrayEquals(expectedSub, actualSub, 0);

		double[] expectedBas = {};
		double[] actualBas = b.getBaseline();
		Assert.assertArrayEquals(expectedBas, actualBas, 0);
	}

	@Test
	public void test_2_2() {
		double[] spec = { 1 };
		String baseType = "Minimum";
		int windowSize = 1;

		Baseline b = new Baseline(spec, baseType, windowSize);

		double[] expectedSub = { 0 };
		double[] actualSub = b.getBaselineSubtracted();
		Assert.assertArrayEquals(expectedSub, actualSub, 0);

		double[] expectedBas = { 1 };
		double[] actualBas = b.getBaseline();
		Assert.assertArrayEquals(expectedBas, actualBas, 0);
	}

	@Test
	public void test_2_3() {
		double[] spec = { 1, 1, 1 };
		String baseType = "Minimum";
		int windowSize = 1;

		Baseline b = new Baseline(spec, baseType, windowSize);

		double[] expectedSub = { 0, 0, 0 };
		double[] actualSub = b.getBaselineSubtracted();
		Assert.assertArrayEquals(expectedSub, actualSub, 0);

		double[] expectedBas = { 1, 1, 1 };
		double[] actualBas = b.getBaseline();
		Assert.assertArrayEquals(expectedBas, actualBas, 0);
	}

	@Test
	public void test_2_4() {
		double[] spec = { 1, 2, 3, 4 };
		String baseType = "Minimum";
		int windowSize = 2;

		Baseline b = new Baseline(spec, baseType, windowSize);

		double[] expectedSub = { 0, 1, 1, 1  };
		double[] actualSub = b.getBaselineSubtracted();
		Assert.assertArrayEquals(expectedSub, actualSub, 0);

		double[] expectedBas = { 1, 1, 2, 3 };
		double[] actualBas = b.getBaseline();
		Assert.assertArrayEquals(expectedBas, actualBas, 0);
	}

	// Test Median Baseline Correction here.

	@Test
	public void test_3_1() {
		double[] spec = {};
		String baseType = "Median";
		int windowSize = 1;

		Baseline b = new Baseline(spec, baseType, windowSize);

		double[] expectedSub = {};
		double[] actualSub = b.getBaselineSubtracted();
		Assert.assertArrayEquals(expectedSub, actualSub, 0);

		double[] expectedBas = {};
		double[] actualBas = b.getBaseline();
		Assert.assertArrayEquals(expectedBas, actualBas, 0);
	}

	@Test
	public void test_3_2() {
		double[] spec = { 1 };
		String baseType = "Median";
		int windowSize = 1;

		Baseline b = new Baseline(spec, baseType, windowSize);

		double[] expectedSub = { 0 };
		double[] actualSub = b.getBaselineSubtracted();
		Assert.assertArrayEquals(expectedSub, actualSub, 0);

		double[] expectedBas = { 1 };
		double[] actualBas = b.getBaseline();
		Assert.assertArrayEquals(expectedBas, actualBas, 0);
	}

	// Test method getDilation()

	@Test
	public void test_4_1() {
		double[] spec = {};
		int structEl = 2;

		double[] expected = {};
		double[] actual = Baseline.getDilation(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_4_2() {
		double[] spec = { 1 };
		int structEl = 3;

		double[] expected = { 1 };
		double[] actual = Baseline.getDilation(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_4_3() {
		double[] spec = { 3, 2, 1, 2, 3 };
		int structEl = 3;

		double[] expected = { 3, 3, 2, 3, 3 };
		double[] actual = Baseline.getDilation(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	// Test method getErosion()

	@Test
	public void test_5_1() {
		double[] spec = {};
		int structEl = 2;

		double[] expected = {};
		double[] actual = Baseline.getErosion(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_5_2() {
		double[] spec = { 1 };
		int structEl = 2;

		double[] expected = { 1 };
		double[] actual = Baseline.getErosion(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_5_3() {
		double[] spec = { 3, 2, 1, 2, 3 };
		int structEl = 2;

		double[] expected = { 2, 1, 1, 1, 2};
		double[] actual = Baseline.getErosion(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	// Test getOpening()

	@Test
	public void test_6_1() {
		double[] spec = {};
		int structEl = 2;

		double[] expected = {};
		double[] actual = Baseline.getOpening(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_6_2() {
		double[] spec = { 1 };
		int structEl = 2;

		double[] expected = { 1 };
		double[] actual = Baseline.getOpening(spec, structEl);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_6_3() {
		double[] spec = { 3, 2, 1, 2, 3 };
		int structEl = 2;

		double[] expected = { 2,1,2,3,3 };
		double[] actual = Baseline.getOpening(spec, structEl);
		System.out.println(Arrays.toString(actual));

		Assert.assertArrayEquals(expected, actual, 0);
	}

}
