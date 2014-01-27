package homework_1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class MedianTrackerReduxTest {
	MedianTrackerRedux med;
	
	@Before
	public void setup(){
		med = new MedianTrackerRedux();
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
		med = new MedianTrackerRedux(2000);
		assertEquals("MedianTrackerRedux is initialized with size 2000",2000,med.getMaxSize());
	}
	
	@Test
	public void testPut_One(){
		med.put(1);
		assertEquals("MedianTrackerRedux has last value 1 after adding 1.",1,med.peek());
	}
	
	@Test 
	public void testGetMedian_Random100(){
		ArrayList<Long> vals = new ArrayList<Long>();
		vals.add(0L);
		vals.add(1L);
		for (int i = 0; i < vals.size(); i ++){
			med.put(vals.get(i));
		}
		MedianTrackerReduxTestCase.addRandomBalanced(med, 50, vals);
		assertEquals("MedianTrackerRedux has value 0.5 after adding 100 elements.", med.getMedian(), med.getMedian(), .001);
	}
	
	@Test 
	public void testGetMedian_Random100000(){
		ArrayList<Long> vals = new ArrayList<Long>();
		vals.add(0L);
		vals.add(1L);
		for (int i = 0; i < vals.size(); i ++){
			med.put(vals.get(i));
		}
		MedianTrackerReduxTestCase.addRandomBalanced(med, 50000, vals);
		assertEquals("MedianTrackerRedux has value 0.5 after adding 100 elements.", med.getMedian(), med.getMedian(), .001);
	}
	
	@Test 
	public void testGetMedian_Random250000(){
		med = new MedianTrackerRedux(1000);
		ArrayList<Long> vals = new ArrayList<Long>();
		vals.add(0L);
		vals.add(1L);
		vals.add(3L);
		vals.add(4L);
		vals.add(5L);
		for (int i = 0; i < vals.size(); i ++){
			med.put(vals.get(i));
		}
		MedianTrackerReduxTestCase.addRandomBalanced(med, 50000, vals);
		assertEquals("MedianTrackerRedux has value 0.5 after adding 100 elements.", med.getMedian(), med.getMedian(), .001);
	}


	@Test
	public void testGetCount_OneHundred() {
		MedianTrackerReduxTestCase.addRange(med,0,100);
		assertEquals("MedianTrackerRedux has count of 100 after adding 100 elements.", 100, med.getCount());
	}
	
	@Test
	public void testGetMedian_TenBackwards() {
		MedianTrackerReduxTestCase.addRangeBackwards(med,9,10);
		assertEquals("MedianTrackerRedux has median of 4.5 after adding 9->0", 4.5, med.getMedian(), .01);
	}
	
	@Test
	public void testGetMedian_TenThousandBackwards() {
		MedianTrackerReduxTestCase.addRangeBackwards(med,9999,10000);
		assertEquals("MedianTrackerRedux has median of 4999.5 after adding 9->0", 4999.5, med.getMedian(), .01);
	}
	
	@Test
	public void testGetMedian_OneHundredEven() {
		MedianTrackerReduxTestCase.addRange(med,0,100);
		assertEquals("MedianTrackerRedux has median 45.5 after adding 0->99.", 49.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_Ten() {
		MedianTrackerReduxTestCase.addRange(med,0,10);
		assertEquals("MedianTrackerRedux has median 4.5 after adding 0->9.", 4.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_SequentialOneMillion(){
		//med.setVerbose(true);
		med = new MedianTrackerRedux(600000);
		MedianTrackerReduxTestCase.addRange(med,0,1000000);
		assertEquals("MedianTrackerRedux has median 499999.5 after adding 0->1000000.", 499999.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_SequentialTenThousand(){
		MedianTrackerReduxTestCase.addRange(med,0,10000);
		assertEquals("MedianTrackerRedux has median 4 after adding 0->1000.", 4999.5, med.getMedian(),.01);
	}
	
	@Test
	public void testGetMedian_Nine() {
		MedianTrackerReduxTestCase.addRange(med,0,9);
		assertEquals("MedianTrackerRedux has median 4 after adding 0->8.", 4.0, med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_None(){
		assertEquals("MedianTrackerRedux has median 0 after adding no values.",-1.0,med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_One(){
		MedianTrackerReduxTestCase.addRange(med,1,1);
		assertEquals("MedianTrackerRedux has median 1 after adding 1.",1.0,med.getMedian(),.01);
	}
	
	@Test 
	public void testGetMedian_SequentialTwo(){
		MedianTrackerReduxTestCase.addRange(med,0,2);
		assertEquals("MedianTrackerRedux has median .5 after adding 0,1",.5,med.getMedian(),.01);
	}


}
