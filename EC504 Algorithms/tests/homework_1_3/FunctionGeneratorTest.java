package homework_1_3;

import static org.junit.Assert.*;
import homework_1_3.FunctionGenerator_Aaron;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Unit tests for the FunctionGenerator_Aaron class
 * 
 * @author Aaron Heuckroth
 * 
 */
public class FunctionGeneratorTest {

	FunctionGenerator_Aaron f = new FunctionGenerator_Aaron();

	/**
	 * Tests to see whether the iterative method of calculating inputs for
	 * 1_3_iv produces the same values as the direct method, for small inputs.
	 */
	@Test
	public void testCalcSet_1_3_iv() {
		int depth = 50;
		long last = 0;
		ArrayList<Long> errors = new ArrayList<Long>();
		for (int i = 0; i <= depth; i++) {
			long std = f.calcSet_1_3_iv(i);
			long iter = f.calcSet_1_3_iv_iterative(last);
			last = iter;
			if (std != iter) {
				System.out.println("Error!: Std: " + std + ", iter: " + iter);
				errors.add(last);
			}
		}
		assertEquals(
				"Values generated by 1_3_iv_iterative same as 1_3_iv for 0->50.",
				0, errors.size());
	}

}
