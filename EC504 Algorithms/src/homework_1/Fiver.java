package homework_1;

import java.util.HashMap;
import java.util.Map;

public class Fiver {
	Map<Long, Boolean> seenNumbers = new HashMap<Long, Boolean>();

	public void seenIt(long num) {
		seenNumbers.put((Long) num, true);
	}

	public boolean hasSeen(long num) {
		return seenNumbers.containsKey((Long) num);
	}

	public long calcV(long offsetNum) {
		long num = offsetNum -3L;
		long output = 0;
		boolean done = false;
		long i = 1L;
		long power = 0L;
		long smallerPower = 0L;
		
		while (!done) {
			smallerPower = power;
			power = (long) Math.pow(2L, i);
			if (num % 2 == 1L) {
				done = true;
				output = 1L;
			} else if (i == 2 && num % power == 2L) {
				done = true;
				output = 3L;
			} else if (num % power == smallerPower - 4L) {
				long newNum = power - 1L;
				if (hasSeen(newNum)) {
					output = newNum;
				} else {
					seenIt(newNum);
					output = 3L;
				}
				done = true;
			} 
			else if (i > 500){
				done = true;
				output = 3;
			}
			
			i++;
		}
		
		if (offsetNum == 0){
			return 0;
		}
		else if (offsetNum == 1){
			return 0;
		}
		else if (offsetNum == 2){
			return 0;
		}
		else if (offsetNum == 3){
			return 1;
		}
		else{
		return output;
		}
	}
}
