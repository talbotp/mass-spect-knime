package msk.test.junit;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.preprocess.Normalizer;

/**
 * Test cases for util.preprocess.Normalizer
 * 
 * @author Andrew P Talbot
 *
 */
public class NormalizerTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test Constructor getters setters etc.

	@Test
	public void test_1_1() {
		double[] spec = {};
		String normType = "invalidNormType";
		
		exception.expect(IllegalArgumentException.class);
		
		@SuppressWarnings("unused")
		Normalizer n = new Normalizer(spec, normType);
	}

	@Test
	public void test_1_2() {
		double[] spec = {};
		String normType = "TIC";

		Normalizer n = new Normalizer(spec, normType);

		double[] expectedSpec = new double[] {};
		double[] actualSpec = n.getSpectrum();
		Assert.assertArrayEquals(expectedSpec, actualSpec, 0);

		String expectedType = normType;
		String actualType = n.getNormType();
		assertEquals(expectedType, actualType);

		n.setNormType("Median");
		expectedType = "Median";
		actualType = n.getNormType();
		assertEquals(expectedType, actualType);

		n.setSpectrum(new double[] { 1, 2, 3 });
		expectedSpec = new double[] { 1, 2, 3 };
		actualSpec = n.getSpectrum();
		Assert.assertArrayEquals(expectedSpec, actualSpec, 0);
	}

	// Test TIC normalization.

	@Test
	public void test_2_1() {
		double[] spec = {};
		String normType = "TIC";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 0;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = {};
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_2_2() {
		double[] spec = { 1 };
		String normType = "TIC";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 1;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_2_3() {
		double[] spec = { 1, 2, 3 };
		String normType = "TIC";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 6;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1d / 6, 2d / 6, 3d / 6 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_2_4() {
		double[] spec = { 1, 9, 0, 0 };
		String normType = "TIC";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 10;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1d / 10, 9d / 10, 0, 0 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	// Now test median Normalization.

	@Test
	public void test_3_1() {
		double[] spec = {};
		String normType = "Median";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 0;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = {};
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_3_2() {
		double[] spec = { 1 };
		String normType = "Median";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 1;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_3_3() {
		double[] spec = { 0 };
		String normType = "Median";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 0;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 0 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_3_4() {
		double[] spec = { 1, 2, 3 };
		String normType = "Median";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 2;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1d / 2, 2d / 2, 3d / 2 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_3_5() {
		double[] spec = { 3, 2, 1 };
		String normType = "Median";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 2;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 3d / 2, 2d / 2, 1d / 2 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_3_6() {
		double[] spec = { 1, 2, 3, 4 };
		String normType = "Median";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 2.5;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1d / 2.5, 2d / 2.5, 3d / 2.5, 4d / 2.5 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	// Test Euclidean Norm

	@Test
	public void test_4_1() {
		double[] spec = {};
		String normType = "Euclidean Norm";

		Normalizer n = new Normalizer(spec, normType);

		exception.expect(IllegalArgumentException.class);
		n.normalizeSpectrum();
	}

	@Test
	public void test_4_2() {
		double[] spec = { 1 };
		String normType = "Euclidean Norm";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 1;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_4_3() {
		double[] spec = { 0 };
		String normType = "Euclidean Norm";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = 0;
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 0 };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_4_4() {
		double[] spec = { 1, 2 };
		String normType = "Euclidean Norm";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = Math.sqrt(5);
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1 / expectedCoeff, 2 / expectedCoeff };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

	@Test
	public void test_4_5() {
		double[] spec = { 1, 2, 3, 4 };
		String normType = "Euclidean Norm";

		Normalizer n = new Normalizer(spec, normType);

		n.normalizeSpectrum();

		double expectedCoeff = Math.sqrt(30);
		double actualCoeff = n.getNormalizationCoefficient();
		assertEquals(expectedCoeff, actualCoeff, 0);

		double[] expectedNorm = { 1 / expectedCoeff, 2 / expectedCoeff, 3 / expectedCoeff, 4 / expectedCoeff };
		double[] actualNorm = n.getSpectrum();
		Assert.assertArrayEquals(expectedNorm, actualNorm, 0);
	}

}
