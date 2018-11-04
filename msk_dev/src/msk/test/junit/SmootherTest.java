package msk.test.junit;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.preprocess.Smoother;

/**
 * JUnit tests for class Smoother.java
 * 
 * NB : We test the methods : - movingMeanSmooth() - triangularMovingMean() BUT
 * NOT savitzkyGolay as we used a library for that.
 * 
 * @author Andrew P Talbot
 */
public class SmootherTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test method movingMeanSmooth().

	@Test
	public void test_1_1() {
		double[] data = new double[0];
		int window = 5;

		exception.expect(IllegalArgumentException.class);
		Smoother.movingMeanSmooth(data, window);
	}

	@Test
	public void test_1_2() {
		double[] data = new double[3];
		int window = -1;

		exception.expect(IllegalArgumentException.class);
		Smoother.movingMeanSmooth(data, window);
	}

	@Test
	public void test_1_3() {
		double[] data = { 1 };
		int window = 3;

		double[] expected = { 1 };
		double[] actual = Smoother.movingMeanSmooth(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_4() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 1;

		double[] expected = { 1, 2, 3, 4, 5 };
		double[] actual = Smoother.movingMeanSmooth(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_5() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 3;

		double[] expected = { 3 / 2d, 2, 3, 4, 9 / 2d };
		double[] actual = Smoother.movingMeanSmooth(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_6() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 2;

		double[] expected = { 3 / 2d, 2, 3, 4, 9 / 2d };
		double[] actual = Smoother.movingMeanSmooth(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_7() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 5;

		double[] expected = { 6 / 3d, 10 / 4d, 15 / 5d, 14 / 4d, 12 / 3d };
		double[] actual = Smoother.movingMeanSmooth(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_1_8() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 4;

		double[] expected = { 6 / 3d, 10 / 4d, 15 / 5d, 14 / 4d, 12 / 3d };
		double[] actual = Smoother.movingMeanSmooth(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	// Test triangularMovingMean()

	@Test
	public void test_2_1() {
		double[] data = new double[0];
		int window = 5;

		exception.expect(IllegalArgumentException.class);
		Smoother.triangularMovingMean(data, window);
	}

	@Test
	public void test_2_2() {
		double[] data = new double[3];
		int window = -1;

		exception.expect(IllegalArgumentException.class);
		Smoother.triangularMovingMean(data, window);
	}

	@Test
	public void test_2_3() {
		double[] data = { 1 };
		int window = 3;

		double[] expected = { 1 };
		double[] actual = Smoother.triangularMovingMean(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_4() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 1;

		double[] expected = { 1, 2, 3, 4, 5 };
		double[] actual = Smoother.triangularMovingMean(data, window);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_5() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 3;

		double[] expected = { (2 * 1 + 1 * 2) / 3d, (1 * 1 + 2 * 2 + 1 * 3) / 4d, (1 * 2 + 2 * 3 + 1 * 4) / 4d,
				(1 * 3 + 2 * 4 + 1 * 5) / 4d, (1 * 4 + 2 * 5) / 3d };
		double[] actual = Smoother.triangularMovingMean(data, window);
		Assert.assertArrayEquals(expected, actual, 0.000001);
	}

	@Test
	public void test_2_6() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 2;

		double[] expected = { (2 * 1 + 1 * 2) / 3d, (1 * 1 + 2 * 2 + 1 * 3) / 4d, (1 * 2 + 2 * 3 + 1 * 4) / 4d,
				(1 * 3 + 2 * 4 + 1 * 5) / 4d, (1 * 4 + 2 * 5) / 3d };
		double[] actual = Smoother.triangularMovingMean(data, window);
		Assert.assertArrayEquals(expected, actual, 0.000001);
	}

	@Test
	public void test_2_7() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 5;

		double[] expected = { (3 * 1 + 2 * 2 + 1 * 3) / 6d, (2 * 1 + 3 * 2 + 2 * 3 + 1 * 4) / 8d,
				(1 * 1 + 2 * 2 + 3 * 3 + 2 * 4 + 1 * 5) / 9d, (1 * 2 + 2 * 3 + 3 * 4 + 2 * 5) / 8d,
				(1 * 3 + 2 * 4 + 3 * 5) / 6d };
		double[] actual = Smoother.triangularMovingMean(data, window);
		Assert.assertArrayEquals(expected, actual, 0.000001);
	}
	
	@Test
	public void test_2_8() {
		double[] data = { 1, 2, 3, 4, 5 };
		int window = 4;

		double[] expected = { (3 * 1 + 2 * 2 + 1 * 3) / 6d, (2 * 1 + 3 * 2 + 2 * 3 + 1 * 4) / 8d,
				(1 * 1 + 2 * 2 + 3 * 3 + 2 * 4 + 1 * 5) / 9d, (1 * 2 + 2 * 3 + 3 * 4 + 2 * 5) / 8d,
				(1 * 3 + 2 * 4 + 3 * 5) / 6d };
		double[] actual = Smoother.triangularMovingMean(data, window);
		Assert.assertArrayEquals(expected, actual, 0.000001);
	}

}
