package homework_1;

import java.util.HashSet;
import java.io.PrintWriter;
import java.io.FileWriter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HomeworkSets {

	MedianTracker med;
	MedianTrackerRedux med2;
	FunctionGenerator f;
	PrintWriter andy;

	@Before
	public void setUp() throws Exception {
		med = new MedianTracker();
		f = new FunctionGenerator();
	}

	@Test
	public void testHomework_i() {
		med.setVerbose(false);
		for (int i = 0; i <= 1006; i++) {
			long nextVal = f.calcSet_1_3_i(i);

			med.put(nextVal);
		}
		System.out
				.println("Homework i: For the set 0->1006 (inclusive), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_i_redux() {
		med2 = new MedianTrackerRedux(700);
		for (int i = 0; i <= 1006; i++) {
			long nextVal = f.calcSet_1_3_i(i);
			med2.put(nextVal);
		}
		System.out
				.println("Homework i: For the set 0->1006 (inclusive), the median is: "
						+ med2.getMedian());
	}

	@Test
	public void testHomework_ii() { // 100136
		med.setVerbose(false);
		for (long i = 0; i <= 10013; i++) {
			long nextVal = f.calcSet_1_3_ii(i);
			med.put(nextVal);
			System.out.println(nextVal + ", " + med.getMedian());
		}
		System.out
				.println("Homework ii: For the set 0->100136 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_ii_redux() { // 100136
		med2 = new MedianTrackerRedux(6000);
		for (long i = 0; i <= 10013; i++) {
			long nextVal = f.calcSet_1_3_ii(i);
			med2.put(nextVal);
			System.out.println(nextVal + ", " + med2.getMedian());
		}
		System.out
				.println("Homework ii: For the set 0->100136 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med2.getMedian());
	}

	@Test
	public void testHomework_iii() {
		med.setVerbose(false);
		MedianTrackerTestCase.addRange(med, 0, 10000764);
		System.out
				.println("Homework iii: For the set 0->1000763 (inclusive), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_iii_redux() {
		med2 = new MedianTrackerRedux(6000000);
		for (int i = 0; i <= 10000764; i++) {
			med2.put(i);
			med.put(i);
			if (med2.getMedian() != med.getMedian()) {
				System.out.println(i + ": med: " + med.getMedian() + ", med2: "
						+ med2.getMedian());
			}

		}
		System.out
				.println("Homework iii: For the set 0->1000763 (inclusive), the median is: "
						+ med.getMedian() + ", med2: " + med2.getMedian());
	}

	@Test
	public void testHomework_v() {
		med.setVerbose(true);
		long last = 0;
		float newMed = 0;
		long nextVal = 0;
		long lastMedCount = 0;
		for (long i = 0; i <= 1000000; i++) {
			// long nextVal = i*2;
			nextVal = f.calcSet_1_3_v(i);
			// long nextVal = (long) (Math.random() * 1000000000000L);
			 newMed = med.put(nextVal);
			last = nextVal;
		}

		System.out
				.println("Homework iv: For the set 0->100136 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_v_redux() {
		med2 = new MedianTrackerRedux(6);
		//String filename = "uniques_v" + System.currentTimeMillis();
		try {
			//andy = new PrintWriter(filename + ".csv");
		} catch (Exception e) {
			System.out.println(e);
		}
		//andy.println("timestamp,median,i,uniques");
		long last = 0;
		float newMed;
		long nextVal = 0;
		long lastMedCount = 0;
		for (long i = 0; i <= 10000000000L; i++) {
			nextVal = f.calcSet_1_3_v(i);
			// System.out.println(nextVal);
			newMed = med2.put(nextVal);
			//System.out.println(i);

			last = nextVal;
			
			if (i % 100000000 == 0) {
				System.out.println(i + ", " + nextVal + ", " + newMed);
			}
		}
		System.out
				.println("Homework v: For the set 0->1 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med2.getMedian());
	}

	@Test
	public void testHomework_iv_redux() {
		med2 = new MedianTrackerRedux(100000);
		long last = 0;
		float newMed;
		HashSet<Long> uniques = new HashSet<Long>();
		for (long i = 0; i <= 10000000000L; i++) {
			// long nextVal = i*2;
			
			long nextVal = f.calcSet_1_3_iv_iterative(last);
			uniques.add(nextVal);
			// long nextVal = (long) (Math.random() * 1000000000000L);
			newMed = med2.put3(nextVal);
			last = nextVal;
			if (i % 100000 == 0) {
				System.out.println(i + ", " + nextVal + ", " + newMed);
				System.out.println("Unique inputs: "+ uniques.size());
			}
		}
		System.out
				.println("Homework iv: For the set 0->100136 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med2.getMedian());
	}

	@Test
	public void testHomework_iv() {
		med.setVerbose(true);
		String filename = "uniques" + System.currentTimeMillis();
		try {
			andy = new PrintWriter(filename + ".csv");
		} catch (Exception e) {
			System.out.println(e);
		}
		andy.println("timestamp,median,i,uniques");
		long last = 0;
		float newMed;
		long lastMedCount = 0;
		HashSet<Float> uniques = new HashSet<Float>();
		for (long i = 0; i <= 100000; i++) {
			// long nextVal = i*2;
			long nextVal = f.calcSet_1_3_iv_iterative(last);
			// long nextVal = (long) (Math.random() * 1000000000000L);
			med.put(nextVal);
			last = nextVal;
			newMed = med.getMedian();
			if (!uniques.contains(newMed)) {
				uniques.add(newMed);
			}
			if (lastMedCount != uniques.size() || i % 1000000 == 0) {
				System.out.println(i + ", " + nextVal + ", " + newMed
						+ ". Unique medians: " + uniques.size());
				andy.println(System.currentTimeMillis() + "," + newMed + ","
						+ i + ", " + uniques.size());
				lastMedCount = uniques.size();
				andy.flush();
			}
		}
		andy.close();
		System.out
				.println("Homework iv: For the set 0->100136 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med.getMedian());
	}
}
