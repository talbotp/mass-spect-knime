package msk.test.junit;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.preprocess.Rebinner;

public class RebinnerTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test generateNewAxis()

	@Test
	public void test_1_1() {
		double minMZ = 0;
		double maxMZ = 0;
		double deltaMZ = 0;

		exception.expect(IllegalArgumentException.class);
		Rebinner.generateNewAxis(minMZ, maxMZ, deltaMZ);
	}

	@Test
	public void test_1_2() {
		double minMZ = 0;
		double maxMZ = 1;
		double deltaMZ = -1;

		exception.expect(IllegalArgumentException.class);
		Rebinner.generateNewAxis(minMZ, maxMZ, deltaMZ);
	}

	@Test
	public void test_1_3() {
		double minMZ = 0;
		double maxMZ = 1;
		double deltaMZ = 1;

		double[] expected = { 0, 1 };
		double[] actual = Rebinner.generateNewAxis(minMZ, maxMZ, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_4() {
		double minMZ = 0;
		double maxMZ = 3;
		double deltaMZ = 1;

		double[] expected = { 0, 1, 2, 3 };
		double[] actual = Rebinner.generateNewAxis(minMZ, maxMZ, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_5() {
		double minMZ = 0;
		double maxMZ = 3;
		double deltaMZ = 2;

		double[] expected = { 0, 2, 3 };
		double[] actual = Rebinner.generateNewAxis(minMZ, maxMZ, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_6() {
		double minMZ = 0;
		double maxMZ = 10;
		double deltaMZ = 5;

		double[] expected = { 0, 5, 10 };
		double[] actual = Rebinner.generateNewAxis(minMZ, maxMZ, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	// Test generateBinnedIntensityArray()

	@Test
	public void test_2_1() {
		double[] M = { 0 };
		double[] S = { 1 };
		double[] Mrebin = { 0 };
		double deltaMZ = 1;

		double[] expected = { 1 };
		double[] actual = Rebinner.generateBinnedIntensityArray(M, S, Mrebin, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_2() {
		double[] M = { 0 };
		double[] S = { 1 };
		double[] Mrebin = { 1 };
		double deltaMZ = 1;

		double[] expected = { 0 };
		double[] actual = Rebinner.generateBinnedIntensityArray(M, S, Mrebin, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}
	
	@Test
	public void test_2_3() {
		double[] M = { 0 };
		double[] S = { 1 };
		double[] Mrebin = { -1 };
		double deltaMZ = 1;

		double[] expected = { 0 };
		double[] actual = Rebinner.generateBinnedIntensityArray(M, S, Mrebin, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}
	
	@Test
	public void test_2_4() {
		double[] M = { 0, 1, 2, 3, 4 };
		double[] S = { 1, 1, 2, 3, 2 };
		double[] Mrebin = { 0, 2, 4 };
		double deltaMZ = 2;

		double[] expected = { 2, 5, 2 };
		double[] actual = Rebinner.generateBinnedIntensityArray(M, S, Mrebin, deltaMZ);
		Assert.assertArrayEquals(expected, actual, 0);
	}


}
