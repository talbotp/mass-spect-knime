package msk.test.junit;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.KeyLogic;

/**
 * Unit testing for class msk.util.KeyLogic
 * 
 * NB : We are unable to test some of the methods in JUnit which use the KNIME
 * data structures.
 * 
 * @author Andrew P Talbot
 */
public class KeyLogicTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	// Test Constructor variants and Getters and toString

	@Test
	public void test_1_1() {
		KeyLogic key = new KeyLogic(1, 2);

		int expectedX = 1;
		int actualX = key.getXPixel();
		assertEquals(expectedX, actualX);

		int expectedY = 2;
		int actualY = key.getYPixel();
		assertEquals(expectedY, actualY);

		String expectedStr = "(1,2)";
		String actualStr = key.toString();
		assertEquals(expectedStr, actualStr);
	}

	@Test
	public void test_1_2() {
		String keyStr = "(4,9)";
		KeyLogic key = new KeyLogic(keyStr);

		int expectedX = 4;
		int actualX = key.getXPixel();
		assertEquals(expectedX, actualX);

		int expectedY = 9;
		int actualY = key.getYPixel();
		assertEquals(expectedY, actualY);

		String expectedStr = "(4,9)";
		String actualStr = key.toString();
		assertEquals(expectedStr, actualStr);
	}

	@Test
	public void test_1_3() {
		String keyStr = "( 1 , 2 )";

		exception.expect(IllegalArgumentException.class);
		@SuppressWarnings("unused")
		KeyLogic key = new KeyLogic(keyStr);
	}

	// Test compareTo

	@Test
	public void test_2_1() {
		KeyLogic key1 = new KeyLogic(1, 1);
		KeyLogic key2 = new KeyLogic(1, 1);

		int expected = 0;

		int actual1 = key1.compareTo(key2);
		assertEquals(expected, actual1);

		int actual2 = key2.compareTo(key1);
		assertEquals(expected, actual2);
	}

	@Test
	public void test_2_2() {
		KeyLogic key1 = new KeyLogic(1, 1);
		KeyLogic key2 = new KeyLogic(2, 1);

		int expected1 = -1;
		int actual1 = key1.compareTo(key2);
		assertEquals(expected1, actual1);

		int expected2 = 1;
		int actual2 = key2.compareTo(key1);
		assertEquals(expected2, actual2);
	}

	@Test
	public void test_2_3() {
		KeyLogic key1 = new KeyLogic(1, 1);
		KeyLogic key2 = new KeyLogic(2, 5);

		int expected1 = -1;
		int actual1 = key1.compareTo(key2);
		assertEquals(expected1, actual1);

		int expected2 = 1;
		int actual2 = key2.compareTo(key1);
		assertEquals(expected2, actual2);
	}
	
	@Test
	public void test_2_4() {
		KeyLogic key1 = new KeyLogic(0, 11);
		KeyLogic key2 = new KeyLogic(25, 5);

		int expected1 = -25;
		int actual1 = key1.compareTo(key2);
		assertEquals(expected1, actual1);

		int expected2 = 25;
		int actual2 = key2.compareTo(key1);
		assertEquals(expected2, actual2);
	}

}
