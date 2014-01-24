package homework_1;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HomeworkTests {
	
	RunningMedian med;

	@Before
	public void setUp() throws Exception {
		med = new RunningMedian();
	}
	
	@Test
	public void testHomework_i() {
		med.setVerbose(false);
		for (int i = 0; i <= 1006; i ++){
			long nextVal = i^(2*i);
			med.put(nextVal);
		}
		System.out
				.println("Homework i: For the set 0->1006 where f(x) = ii xor (2*ii), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_ii() {
		med.setVerbose(false);
		for (int i = 0; i <= 100136; i ++){
			long nextVal = (long)((i % 1000)/(Math.sin(i)));
			med.put(nextVal);
		}
		System.out
				.println("Homework ii: For the set 0->100136 (inclusive) where f(n) is (i%1000)/sin(i), the median is: "
						+ med.getMedian());
	}

	@Test
	public void testHomework_iii() {
		med.setVerbose(false);
		RunningMedianTestCase.addRange(med,0, 10000764);
		System.out
				.println("Homework iii: For the set 0->1000763 (inclusive), the median is: "
						+ med.getMedian());
	}
}
