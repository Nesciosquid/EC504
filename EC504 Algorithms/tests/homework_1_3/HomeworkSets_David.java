package homework_1_3;

import homework_1_3.FunctionGenerator_David;
import homework_1_3.MedianTracker;

import org.junit.Before;
import org.junit.Test;

/** "Tests" that run David Klaus' problem sets for 1.3.i-v
 * 
 * @author Aaron Heuckroth
 *
 */
public class HomeworkSets_David {

	MedianTracker med;
	FunctionGenerator_David f;
	long limit;

	@Before
	public void setUp() throws Exception {
		f = new FunctionGenerator_David();
	}

	@Test
	public void testHomework_1_3_i() {
		med = new MedianTracker(600);
		limit = 1004;
		for (int i = 0; i <= limit; i++) {
			long nextVal = f.calcSet_1_3_i(i);
			med.put(nextVal);
		}
		System.out
				.println("Homework i: For the set 0->1006 (inclusive) where f(n) = n^(2n), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_1_3_ii() { // 100136
		med = new MedianTracker(6000);
		limit = 1000264;
		for (long i = 0; i <= limit; i++) {
			long nextVal = f.calcSet_1_3_ii(i);
			med.put(nextVal);
		}
		System.out
				.println("Homework ii: For the set 0->100136 (inclusive) where f(n) = (i%1000)/sin(n), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_1_3_iii() {
		long startTime = System.currentTimeMillis();
		med = new MedianTracker(6000000);
		limit = 10000342;
		for (long i = 0; i <= limit; i++){
			long nextVal = f.calcSet_1_3_iii(i);
			med.put(nextVal);
			if (i % 100000 == 0){
				MedianTrackerTestCase.statusReport(startTime, System.currentTimeMillis(), i, limit);
			}
		}
		
		System.out
				.println("Homework iii: For the set 0->1000763 (inclusive) where f(n) = n, the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_1_3_iv() {
		long startTime = System.currentTimeMillis();
		med = new MedianTracker(100000);
		limit = 10000569855L;
		long last = 0;
		for (long i = 0; i <= limit; i++) {
			long nextVal = f.calcSet_1_3_iv_iterative(last);
			med.put(nextVal);
			last = nextVal;
			if (i % 10000000 == 0) {
				MedianTrackerTestCase.statusReport(startTime, System.currentTimeMillis(), i, limit);
			}
		}
		System.out
				.println("Homework iv: For the set 0->10000125463 (inclusive) where f(n) is pow(2,n) % 112582705942171, the median is: "
						+ med.getMedian().toString());
		System.out.println("Left Index: " + med.getMedianLeft());
		System.out.println("Right Index: " + med.getMedianRight());
	}

	@Test
	public void testHomework_1_3_v() {
		long startTime = System.currentTimeMillis();
		med = new MedianTracker(6);
		limit = 100000087569L;
		long nextVal = 0;
		for (long i = 0; i <= limit; i++) {
			nextVal = f.calcSet_1_3_v(i);
			med.put(nextVal);
			if (i % 100000000 == 0) {
				MedianTrackerTestCase.statusReport(startTime, System.currentTimeMillis(), i, limit);
			}
		}
		System.out
				.println("Homework v: For the set 0->1 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med.getMedian());
	}

}
