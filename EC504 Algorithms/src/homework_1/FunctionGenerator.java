package homework_1;

import java.util.HashMap;
import java.util.Map;
import java.math.*;

public class FunctionGenerator {
	
	public long calcSet_1_3_i(long ii){
		return (ii ^ (ii<<1));
	}
	
	public long calcSet_1_3_ii(long ii){
		return (long)((ii%1000)/Math.sin(ii));
	}
	
	public long calcSet_1_3_iii(long ii){
		return ii;
	}
	
	public long calcSet_1_3_iv(long ii){
		long modulus = 112582705942171L;
		return ((long)Math.pow(2,ii) % modulus);
	}
	
	private long doubleModulo(long lastValue, long modulus){
		if (lastValue == 0){
			return 1 % modulus;
		}
		else return (lastValue * 2) % modulus;
	}
	
	private long tripleModulo(long lastValue, long modulus){
		if (lastValue == 0){
			return 1 % modulus;
		}
		else return (lastValue * 3) % modulus;
	}
	
	public long calcSet_1_3_iv_iterative(long lastValue){
		return doubleModulo(lastValue, 112582705942171L);
	}
	
	public long calcSet_1_3_iv_forAndy(long lastValue){
		return doubleModulo(lastValue, 711282745943161L);
	}
	
	public long calcSet_1_3_iv_forAndy2(long lastValue){
		return tripleModulo(lastValue, 112582705942171L);
	}
	
	public long calcSet_1_3_v(long ii) {
		if ((ii - 1) <= 0) {
			return 0;
		} else {
			return (ii ^ (ii + 1)) % (ii - 1);
		}
	}
	

}
