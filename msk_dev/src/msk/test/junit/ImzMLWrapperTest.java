package msk.test.junit;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import msk.util.ImzMLWrapper;
import mzML.Spectrum;

/**
 * JUnit test cases for class ImzMLWrapper which acts as the imzML Reader.
 * 
 * In particular, we are interested in testing the SpectrumIterator that it creates.
 * 
 * @author Andrew P Talbot
 */
public class ImzMLWrapperTest {

	// This is a basic test synthetic dataset.
	private static final String file = "data/Example_Continuous.imzML"; 
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	// Test method ImzMLWrapper.isImzMLFileExtension()

	@Test
	public void test_1_1() {
		String imzMLPath = "InvalidImzMLExtension.hello";
		
		exception.expect(IllegalArgumentException.class);
		ImzMLWrapper.isImzMLFileExtension(imzMLPath);
	}
	
	@Test
	public void test_1_2() {
		String imzMLPath = "Valid.imzML";
		
		// We don't expect any exception.
		ImzMLWrapper.isImzMLFileExtension(imzMLPath);
	}
	
	@Test
	public void test_1_3() {
		String imzMLPath = "Valid.imzML.imzML";
		
		// We don't expect any exception.
		ImzMLWrapper.isImzMLFileExtension(imzMLPath);
	}
	
	@Test
	public void test_1_4() {
		String imzMLPath = "./Hello/dir/file/Valid.imzML.imzML";
		
		// We don't expect any exception.
		ImzMLWrapper.isImzMLFileExtension(imzMLPath);
	}
	
	// Test method ImzMLWrapper.getFilename()
	
	@Test
	public void test_2_1() { 
		String file = "test.imzML";
		
		String expected = "test";
		String actual = ImzMLWrapper.getFileName(file);
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_2_2() {
		String file = "hello.test.imzML";
		
		String expected = "hello.test";
		String actual = ImzMLWrapper.getFileName(file);
		assertEquals(expected, actual);
	}
	
	// Test iterator. using 3x3 imzML dataset.
	
	@Test
	public void test_3_1() {
		boolean t = true;
		boolean f = false;
		
		ImzMLWrapper reader = new ImzMLWrapper(file);	
		Iterator<Spectrum> iter = reader.iterator();
		
		boolean actualB11 = iter.hasNext();
		assertEquals(t, actualB11);
		Spectrum expected11 = reader.getImzML().getSpectrum(1, 1);
		Spectrum actual11 = iter.next();		
		assertEquals(expected11, actual11);
		
		boolean actualB21 = iter.hasNext();
		assertEquals(t, actualB21);
		Spectrum expected21 = reader.getImzML().getSpectrum(2, 1);
		Spectrum actual21 = iter.next();	
		assertEquals(expected21, actual21);
		
		boolean actualB31 = iter.hasNext();
		assertEquals(t, actualB31);
		Spectrum expected31 = reader.getImzML().getSpectrum(3, 1);
		Spectrum actual31 = iter.next();	
		assertEquals(expected31, actual31);
		
		boolean actualB12 = iter.hasNext();
		assertEquals(t, actualB12);
		Spectrum expected12 = reader.getImzML().getSpectrum(1, 2);
		Spectrum actual12 = iter.next();	
		assertEquals(expected12, actual12);
		
		boolean actualB22 = iter.hasNext();
		assertEquals(t, actualB22);
		Spectrum expected22 = reader.getImzML().getSpectrum(2, 2);
		Spectrum actual22 = iter.next();	
		assertEquals(expected22, actual22);
		
		boolean actualB32 = iter.hasNext();
		assertEquals(t, actualB32);
		Spectrum expected32 = reader.getImzML().getSpectrum(3, 2);
		Spectrum actual32 = iter.next();	
		assertEquals(expected32, actual32);
		
		boolean actualB13 = iter.hasNext();
		assertEquals(t, actualB13);
		Spectrum expected13 = reader.getImzML().getSpectrum(1, 3);
		Spectrum actual13 = iter.next();
		assertEquals(expected13, actual13);
		
		boolean actualB23 = iter.hasNext();
		assertEquals(t, actualB23);
		Spectrum expected23 = reader.getImzML().getSpectrum(2, 3);
		Spectrum actual23 = iter.next();		
		assertEquals(expected23, actual23);
		
		boolean actualB33 = iter.hasNext();
		assertEquals(t, actualB33);
		Spectrum expected33 = reader.getImzML().getSpectrum(3, 3);
		Spectrum actual33 = iter.next();		
		assertEquals(expected33, actual33);
		
		// Now we expect hasNext() to return false
		boolean actualF = iter.hasNext();
		assertEquals(f, actualF);
	}
	
}
