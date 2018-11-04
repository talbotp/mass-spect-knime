package msk.test.junit;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.cluster.MSKClusterValues;

public class MSKClusterValuesTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test parseCluster()

	@Test
	public void test_1_1() {
		String s = "cluster_1";

		int expected = 1;
		int actual = MSKClusterValues.parseCluster(s);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_2() {
		String s = "cluster_1_";

		exception.expect(NumberFormatException.class);
		@SuppressWarnings("unused")
		int actual = MSKClusterValues.parseCluster(s);
	}

	@Test
	public void test_1_3() {
		String s = "cluster_5";

		int expected = 5;
		int actual = MSKClusterValues.parseCluster(s);
		assertEquals(expected, actual);
	}

	@Test
	public void test_1_4() {
		String s = "cluster_-15";

		int expected = -15;
		int actual = MSKClusterValues.parseCluster(s);
		assertEquals(expected, actual);
	}

	// Test parseClusterArray()

	@Test
	public void test_2_1() {
		String[] clusterStrings = new String[] { "cluster_1" };

		double[] expected = { 1 };
		double[] actual = MSKClusterValues.parseClusterArray(clusterStrings);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_2() {
		String[] clusterStrings = new String[] { "cluster_1", "cluster_2", "cluster_3" };

		double[] expected = { 1, 2, 3 };
		double[] actual = MSKClusterValues.parseClusterArray(clusterStrings);

		Assert.assertArrayEquals(expected, actual, 0);
	}

	@Test
	public void test_2_3() {
		String[] clusterStrings = new String[] { "cluster_1", "cluster_2", "cluster_-15", "cluster_11" };

		double[] expected = { 1, 2, -15, 11 };
		double[] actual = MSKClusterValues.parseClusterArray(clusterStrings);

		Assert.assertArrayEquals(expected, actual, 0);
	}

}
