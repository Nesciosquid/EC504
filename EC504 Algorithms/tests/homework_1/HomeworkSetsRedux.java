package homework_1;

import java.util.HashSet;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

public class HomeworkSetsRedux {

	MedianTracker med;
	MedianTrackerRedux med2;
	FunctionGenerator f;
	PrintWriter andy;
	long limit;

	@Before
	public void setUp() throws Exception {
		f = new FunctionGenerator();
	}

	@Test
	public void testHomework_i_redux() {
		long startTime = System.currentTimeMillis();
		med2 = new MedianTrackerRedux(600);
		limit = 1006;
		for (int i = 0; i <= limit; i++) {
			long nextVal = f.calcSet_1_3_i(i);
			med2.put3(nextVal);
		}
		System.out
				.println("Homework i: For the set 0->1006 (inclusive) where f(n) = n^(2n), the median is: "
						+ med2.getMedian());
	}

	@Test
	public void testHomework_ii_redux() { // 100136
		long startTime = System.currentTimeMillis();
		med2 = new MedianTrackerRedux(6000);
		limit = 1000136;
		for (long i = 0; i <= limit; i++) {
			long nextVal = f.calcSet_1_3_ii(i);
			med2.put3(nextVal);
		}
		System.out
				.println("Homework ii: For the set 0->100136 (inclusive) where f(n) = (i%1000)/sin(n), the median is: "
						+ med2.getMedian());
	}

	@Test
	public void testHomework_iii_redux() {
		long startTime = System.currentTimeMillis();
		med2 = new MedianTrackerRedux(6000000);
		limit = 10000763;
		for (long i = 0; i <= limit; i++){
			long nextVal = f.calcSet_1_3_iii(i);
			med2.put3(nextVal);
			if (i % 100000 == 0){
				MedianTrackerReduxTestCase.statusReport(startTime, System.currentTimeMillis(), i, limit);
			}
		}
		
		System.out
				.println("Homework iii: For the set 0->1000763 (inclusive) where f(n) = n, the median is: "
						+ med.getMedian() + ", med2: " + med2.getMedian());
	}

	@Test
	public void testHomework_iv_redux() {
		long startTime = System.currentTimeMillis();
		med2 = new MedianTrackerRedux(100000);
		limit = 10000125463L;
		long last = 0;
		float newMed;
		for (long i = 0; i <= limit; i++) {
			long nextVal = f.calcSet_1_3_iv_iterative(last);
			newMed = med2.put3(nextVal);
			last = nextVal;
			if (i % 10000000 == 0) {
				MedianTrackerReduxTestCase.statusReport(startTime, System.currentTimeMillis(), i, limit);
			}
		}
		System.out
				.println("Homework iv: For the set 0->10000125463 (inclusive) where f(n) is pow(2,n) % 112582705942171, the median is: "
						+ med2.getMedian());
	}

	@Test
	public void testHomework_v_redux() {
		long startTime = System.currentTimeMillis();
		med2 = new MedianTrackerRedux(6);
		float newMed;
		limit = 100000159019L;
		long nextVal = 0;
		for (long i = 0; i <= limit; i++) {
			nextVal = f.calcSet_1_3_v(i);
			newMed = med2.put3(nextVal);
			if (i % 100000000 == 0) {
				MedianTrackerReduxTestCase.statusReport(startTime, System.currentTimeMillis(), i, limit);
			}
		}
		System.out
				.println("Homework v: For the set 0->1 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med2.getMedian());
	}

}
