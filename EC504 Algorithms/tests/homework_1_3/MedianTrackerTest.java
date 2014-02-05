package homework_1_3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import homework_1_3.MedianTracker;

import java.util.ArrayList;


/** 
 * Tests to ensure that the MedianTracker class is working correctly.
 * Includes both unit tests and sample sets with known arithmetic medians.
 * @author Aaron Heuckroth
 *
 */
public class MedianTrackerTest {
	MedianTracker med;
	String lastTest = "";
	
	@Before
	public void setup(){
		med = new MedianTracker();
	}
	
	
	@Test
	public void testGetCount_Init(){
		assertEquals("MedianTrackerRedux is initialized with count 0",0,med.getCount());
	}
	
	@Test
	public void testSize_Init(){
		assertEquals("MedianTrackerRedux is initialized with default size 1000000",1000000,med.getMaxSize());
	}
	
	@Test
	public void testSize_Init2000(){
		med = new MedianTracker(2000);
		assertEquals("MedianTrackerRedux is initialized with size 2000",2000,med.getMaxSize());
	}
	
	/**
	 * Generates a random sequence of 50 each of 1's and 0's, which will have the known median 
	 * of 0.5.
	 */
	@Test 
	public void testGetMedian_Random100(){
		ArrayList<Long> vals = new ArrayList<Long>();
		vals.add(0L);
		vals.add(1L);
		for (int i = 0; i < vals.size(); i ++){
			med.put(vals.get(i));
		}
		MedianTrackerTestCase.addRandomBalanced(med, 50, vals);
		assertEquals("MedianTrackerRedux has value 0.5 after adding 100 elements.", 0.5, med.getMedian(), .001);
	}
	
	/**
	 * Generates a random sequence of 50000 each of 1's and 0's, which will have the known median 
	 * of 0.5.
	 */
	@Test 
	public void testGetMedian_Random100000(){
		ArrayList<Long> vals = new ArrayList<Long>();
		vals.add(0L);
		vals.add(1L);
		for (int i = 0; i < vals.size(); i ++){
			med.put(vals.get(i));
		}
		MedianTrackerTestCase.addRandomBalanced(med, 50000, vals);
		assertEquals("MedianTrackerRedux has value 0.5 after adding 100000 elements.", 0.5, med.getMedian(), .001);
	}
	
	/**
	 * Generates a random sequence of 50000 each of values 1-5, which will have the known median 
	 * of 3.
	 * THIS METHOD CAN FAIL if, by chance, more than 1000 unique medians must be stored. 
	 * If that is the case, increase the size of the MedianTracker to a larger value.
	 */
	@Test 
	public void testGetMedian_Random250000(){
		med = new MedianTracker(1000);
		ArrayList<Long> vals = new ArrayList<Long>();
		vals.add(1L);
		vals.add(2L);
		vals.add(3L);
		vals.add(4L);
		vals.add(5L);
		for (int i = 0; i < vals.size(); i ++){
			med.put(vals.get(i));
		}
		MedianTrackerTestCase.addRandomBalanced(med, 50000, vals);
		assertEquals("MedianTrackerRedux has value 3 after adding 100 elements.", 3, med.getMedian(), .001);
	}


	@Test
	public void testGetCount_OneHundred() {
		MedianTrackerTestCase.addRange(med,0,100);
		assertEquals("MedianTrackerRedux has count of 100 after adding 100 elements.", 100, med.getCount());
	}
	
	@Test
	public void testGetMedian_TenBackwards() {
		MedianTrackerTestCase.addRangeBackwards(med,9,10);
		assertEquals("MedianTrackerRedux has median of 4.5 after adding 9->0", 4.5, med.getMedian(), .01);
	}
	
	@Test
	public void testGetMedian_TenThousandBackwards() {
		MedianTrackerTestCase.addRangeBackwards(med,9999,10000);
		assertEquals("MedianTrackerRedux has median of 4999.5 after adding 9->0", 4999.5, med.getMedian(), .01);
	}
	
	@Test
	public void testGetMedian_OneHundredEven() {
		MedianTrackerTestCase.addRange(med,0,100);
		assertEquals("MedianTrackerRedux has median 45.5 after adding 0->99.", 49.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_Ten() {
		MedianTrackerTestCase.addRange(med,0,10);
		assertEquals("MedianTrackerRedux has median 4.5 after adding 0->9.", 4.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_SequentialOneMillion(){
		//med.setVerbose(true);
		med = new MedianTracker(600000);
		MedianTrackerTestCase.addRange(med,0,1000000);
		assertEquals("MedianTrackerRedux has median 499999.5 after adding 0->1000000.", 499999.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_SequentialTenThousand(){
		MedianTrackerTestCase.addRange(med,0,10000);
		assertEquals("MedianTrackerRedux has median 4 after adding 0->1000.", 4999.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_Nine() {
		MedianTrackerTestCase.addRange(med,0,9);
		assertEquals("MedianTrackerRedux has median 4 after adding 0->8.", 4.0, med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_One(){
		MedianTrackerTestCase.addRange(med,1,1);
		assertEquals("MedianTrackerRedux has median 1 after adding 1.",1.0,med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_SequentialTwo(){
		MedianTrackerTestCase.addRange(med,0,2);
		assertEquals("MedianTrackerRedux has median .5 after adding 0,1",.5,med.getMedian(),.01);
	}


}
