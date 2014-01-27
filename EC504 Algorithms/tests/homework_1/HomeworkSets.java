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
	public void testHomework_iii() {
		med.setVerbose(false);
		MedianTrackerTestCase.addRange(med, 0, 10000764);
		System.out
				.println("Homework iii: For the set 0->1000763 (inclusive), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_v() {
		med.setVerbose(true);
		long nextVal = -1;
		float newMed = -1;
		for (long i = 0; i <= 1000000; i++) {
			nextVal = f.calcSet_1_3_v(i);
			newMed = med.put(nextVal);
			if (i % 10000 == 0) {
				System.out.println("i: " + i + ", new value:" + nextVal + ", new median:" + newMed);
			}
		}
		System.out
				.println("Homework iv: For the set 0->100136 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ newMed);
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
