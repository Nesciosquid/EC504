package homework_1;

public class MedianTrackerTestCase {
	
	public static void addHundred(MedianTracker m){
		for (int i = 0; i < 100; i ++){
			m.put(i);
		}
	}
	
	public static void addRange(MedianTracker m, int start, int number){
		for (int i = start; i < start + number; i ++){
			m.put(i);
		}
	}
	
	public static void addRangeBackwards(MedianTracker m, int start, int number){
		for (int i = 0; i < number; i ++){
			m.put(start-i);
		}
	}

}
