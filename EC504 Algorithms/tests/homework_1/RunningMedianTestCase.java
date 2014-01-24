package homework_1;

public class RunningMedianTestCase {
	
	public static void addHundred(RunningMedian m){
		for (int i = 0; i < 100; i ++){
			m.put(i);
		}
	}
	
	public static void addRange(RunningMedian m, int start, int number){
		for (int i = start; i < start + number; i ++){
			m.put(i);
		}
	}
	
	public static void addRangeBackwards(RunningMedian m, int start, int number){
		for (int i = 0; i < number; i ++){
			m.put(start-i);
		}
	}
	
	/* This is dumb, since I can't assert a random number! */
	public static void addRandom(RunningMedian m, long high, int number){
		for (int i = 0; i < number; i ++){
			m.put((long)(double)Math.random()*high);
		}
	}

}
