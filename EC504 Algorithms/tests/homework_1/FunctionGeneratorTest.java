package homework_1;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class FunctionGeneratorTest {
	
	FunctionGenerator f = new FunctionGenerator();

	@Test
	public void testCalcSet_1_3_iv() {
		int depth = 50;
		long last = 0;
		ArrayList<Long> errors = new ArrayList<Long>();
		for (int i = 0; i <= depth; i ++){
			long std = f.calcSet_1_3_iv(i);
			long iter = f.calcSet_1_3_iv_iterative(last);
			last = iter;
			if (std != iter){
				System.out.println("Error!: Std: " + std + ", iter: " +iter);
				errors.add(last);
			}
		}
		assertEquals("Values generated by 1_3_iv_iterative same as 1_3_iv for 0->50.", 0, errors.size());
	}
	
	@Test
	public void testCalcSet_1_3_v(){
		int depth = 100000;
		long last = 0;
		ArrayList<Long> errors = new ArrayList<Long>();
		for (int i = 0; i <= depth; i ++){
			long std = f.calcSet_1_3_v(i);
			System.out.println(std);
		}
	}
	

}
