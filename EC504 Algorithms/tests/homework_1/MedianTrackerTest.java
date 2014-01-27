package homework_1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MedianTrackerTest {
	
	MedianTracker med;

	@Before
	public void setUp() throws Exception {
		med = new MedianTracker();
		
	}
	
	@Test
	public void testCount_Init(){
		assertEquals("RunningMedian is initialized with count 0",0,med.getCount());
	}
	
	@Test
	public void testSize_Init(){
		assertEquals("RunningMedian is initialized with size 0",0,med.getSize());
	}
	
	@Test
	public void testPut_One(){
		med.put(1);
		assertEquals("RunningMedian has last value 1 after adding 1.",1,med.peek());
	}

	@Test
	public void testGetCount_OneHundred() {
		MedianTrackerTestCase.addHundred(med);
		assertEquals("RunningMedian has count of 100 after adding 100 elements.", 100, med.getCount());
	}
	
	@Test
	public void testGetSize_OneHundred() {
		MedianTrackerTestCase.addHundred(med);
		assertEquals("RunningMedian has size of 100 after adding 100 elements.", 100, med.getSize());
	}
	
	@Test
	public void testGetMedian_TenBackwards() {
		MedianTrackerTestCase.addRangeBackwards(med,9,10);
		assertEquals("RunningMedian has median of 4.5 after adding 9->0", 4.5, med.getMedian(), .01);
	}
	
	@Test
	public void testGetMedian_TenThousandBackwards() {
		MedianTrackerTestCase.addRangeBackwards(med,9999,10000);
		assertEquals("RunningMedian has median of 4.5 after adding 9->0", 4999.5, med.getMedian(), .01);
	}
	
	@Test
	public void testGetMedian_OneHundredEven() {
		MedianTrackerTestCase.addHundred(med);
		assertEquals("RunningMedian has median 45.5 after adding 0->99.", 49.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_Ten() {
		MedianTrackerTestCase.addRange(med,0,10);
		assertEquals("RunningMedian has median 4.5 after adding 0->9.", 4.5, med.getMedian(),.01);
	}
	
	@Test
	public void testIsEven_Odd(){
		MedianTrackerTestCase.addRange(med,0,9);
		assert(!med.isEven());
	}
	
	@Test
	public void testIsEven_Even(){
		MedianTrackerTestCase.addRange(med,0,10);
		assert(med.isEven());
	}
	
	@Test
	public void testGetMedian_SequentialOneMillion(){
		//med.setVerbose(true);
		MedianTrackerTestCase.addRange(med,0,1000000);
		assertEquals("RunningMedian has median 499999.5 after adding 0->1000000.", 499999.5, med.getMedian(),.01);
	}
	
	/*
	@Test
	public void testGetMedian_SequentialOneBillion(){
		med.setVerbose(true);
		MedianTrackerTestCase.addRange(med,0,1000000000);
		assertEquals("RunningMedian has median 499999.5 after adding 0->1000000.", 499999.5, med.getMedian(),.01);
	}
	*/
	
	
	@Test
	public void testGetMedian_SequentialTenThousand(){
		MedianTrackerTestCase.addRange(med,0,10000);
		assertEquals("RunningMedian has median 4 after adding 0->1000.", 4999.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_Nine() {
		MedianTrackerTestCase.addRange(med,0,9);
		assertEquals("RunningMedian has median 4 after adding 0->8.", 4.0, med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_None(){
		assertEquals("RunningMedian has median 0 after adding no values.",0.0,med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_One(){
		MedianTrackerTestCase.addRange(med,1,1);
		assertEquals("RunningMedian has median 1 after adding 1.",1.0,med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_SequentialTwo(){
		MedianTrackerTestCase.addRange(med,0,2);
		assertEquals("RunningMedian has median .5 after adding 0,1",.5,med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMiddle_SequentialTwo(){
		MedianTrackerTestCase.addRange(med,0,2);
		assertEquals("Center index of two-value median array is 0.",0,med.getMiddle());
	}
	
	@Test 
	public void testGetMiddle_SequentialTen(){
		MedianTrackerTestCase.addRange(med,0,10);
		assertEquals("Center index of ten-value median array is 4.",4,med.getMiddle());
	}
	
	@Test 
	public void testGetMiddle_SequentialNine(){
		MedianTrackerTestCase.addRange(med,0,9);
		assertEquals("Center index of nine-value median array is 4.",4,med.getMiddle());
	}



}
