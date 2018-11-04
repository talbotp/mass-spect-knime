package msk.test.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.MathMSK;

/**
 * Test cases for msk.util.MathMSK
 * 
 * @author parker
 *
 */
public class MathMSKTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test binarySearchClosest()

	@Test
	public void test_1_1() {
		double[] arr = {};
		double val = 0;

		exception.expect(IllegalArgumentException.class);
		MathMSK.binarySearchClosest(arr, val);
	}

	@Test
	public void test_1_2() {
		double[] arr = { 1 };
		double val = 0;

		int expected = 0;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_3() {
		double[] arr = { 1 };
		double val = 1;

		int expected = 0;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_4() {
		double[] arr = { 1, 2 };
		double val = 1;

		int expected = 0;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_5() {
		double[] arr = { 1, 2 };
		double val = 2;

		int expected = 1;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_6() {
		double[] arr = { 1, 2 };
		double val = 1.5;

		int expected = 0;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_7() {
		double[] arr = { 1, 2 };
		double val = 1.5;

		int expected = 0;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_8() {
		double[] arr = { 1, 2, 3 };
		double val = 100;

		int expected = 2;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_9() {
		double[] arr = { 1, 2, 3 };
		double val = -100;

		int expected = 0;
		int actual = MathMSK.binarySearchClosest(arr, val);
		assertEquals(expected, actual);
	}

	// Test removeNegatives()

	@Test
	public void test_2_1() {
		double[] arr = {};

		double[] expected = {};
		double[] actual = MathMSK.removeNegatives(arr);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_2() {
		double[] arr = { 1, 2, 3 };

		double[] expected = { 1, 2, 3 };
		double[] actual = MathMSK.removeNegatives(arr);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_3() {
		double[] arr = { -1, 2, 3 };

		double[] expected = { 0, 2, 3 };
		double[] actual = MathMSK.removeNegatives(arr);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_4() {
		double[] arr = { -1, -2, -3 };

		double[] expected = { 0, 0, 0 };
		double[] actual = MathMSK.removeNegatives(arr);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_5() {
		double[] arr = { 0, 0, 0 };

		double[] expected = { 0, 0, 0 };
		double[] actual = MathMSK.removeNegatives(arr);
		Assert.assertArrayEquals(expected, actual, 0);
	}

	// Test l2Norm()

	@Test
	public void test_3_1() {
		double[] arr = {};

		exception.expect(IllegalArgumentException.class);
		MathMSK.l2Norm(arr);
	}

	@Test
	public void test_3_2() {
		double[] arr = { 1d };

		double expected = 1;
		double actual = MathMSK.l2Norm(arr);
		assertEquals(expected, actual, 0);
	}

	@Test
	public void test_3_3() {
		double[] arr = { 0d };

		double expected = 0;
		Double actual = MathMSK.l2Norm(arr);
		assertEquals(expected, actual, 0);
	}

	@Test
	public void test_3_4() {
		double[] arr = { 1d, 2d };

		double expected = Math.sqrt(5);
		double actual = MathMSK.l2Norm(arr);
		assertEquals(expected, actual, 0);
	}

	@Test
	public void test_3_5() {
		double[] arr = { -1d, -2d };

		double expected = Math.sqrt(5);
		double actual = MathMSK.l2Norm(arr);
		assertEquals(expected, actual, 0);
	}

	@Test
	public void test_3_6() {
		double[] arr = { -1d, -2d, 3d };

		double expected = Math.sqrt(14);
		double actual = MathMSK.l2Norm(arr);
		assertEquals(expected, actual, 0);
	}

	// Test l2NormDiff() - REQUIRE : input arrays are the same length.

	@Test
	public void test_4_1() {
		double[] arr1 = {};
		double[] arr2 = {};

		exception.expect(IllegalArgumentException.class);
		MathMSK.l2NormDiff(arr1, arr2);
	}

	@Test
	public void test_4_2() {
		double[] arr1 = { 1 };
		double[] arr2 = { 1 };

		double expected = 0;
		double actual = MathMSK.l2NormDiff(arr1, arr2);
		assertEquals(expected, actual, 0);
	}

	@Test
	public void test_4_3() {
		double[] arr1 = { 1, 2, 3 };
		double[] arr2 = { 1, 2, 3 };

		double expected = 0;
		double actual = MathMSK.l2NormDiff(arr1, arr2);
		assertEquals(expected, actual, 0);
	}

	@Test
	public void test_4_4() {
		double[] arr1 = { 1, 2, 3 };
		double[] arr2 = { 0, 0, 0 };

		double expected1 = Math.sqrt(14);
		double actual1 = MathMSK.l2NormDiff(arr1, arr2);
		assertEquals(expected1, actual1, 0);
		
		double expected2 = Math.sqrt(14);
		double actual2 = MathMSK.l2NormDiff(arr2, arr1);
		assertEquals(expected2, actual2, 0);
	}
	
	@Test
	public void test_4_5() {
		double[] arr1 = { 1, 2, 3 };
		double[] arr2 = { 8, 9, 7 };

		double expected1 = Math.sqrt(114);
		double actual1 = MathMSK.l2NormDiff(arr1, arr2);
		assertEquals(expected1, actual1, 0);
		
		double expected2 = Math.sqrt(114);
		double actual2 = MathMSK.l2NormDiff(arr2, arr1);
		assertEquals(expected2, actual2, 0);
	}
	
	@Test
	public void test_4_6() {
		double[] arr1 = { -1, -2, -3 };
		double[] arr2 = { 2, 2, 2 };

		double expected1 = Math.sqrt(50);
		double actual1 = MathMSK.l2NormDiff(arr1, arr2);
		assertEquals(expected1, actual1, 0);
		
		double expected2 = Math.sqrt(50);
		double actual2 = MathMSK.l2NormDiff(arr2, arr1);
		assertEquals(expected2, actual2, 0);
	}

}
