package homework_1;

import java.util.ArrayList;

public class MedianTrackerReduxTestCase {
	
	
	public static void addRange(MedianTrackerRedux m, int start, int number){
		for (int i = start; i < start + number; i ++){
			m.put3(i);
		}
	}
	
	public static void addRandomBalanced(MedianTrackerRedux m, long each, ArrayList<Long> values){
		ArrayList<Long> picked = new ArrayList<Long>();
		for (int i = 0; i < values.size(); i ++){
			picked.add(i, 0L);
		}
		long end = values.size() * each;
		for (long i = 0; i < end; i++){
			int choose = (int)Math.floor(Math.random() * values.size());
			m.put3(values.get(choose));
			picked.set(choose, picked.get(choose) + 1);
			if (picked.get(choose) == each){
				picked.remove(choose);
				values.remove(choose);
			}
		}
	}
	
	public static void addRangeBackwards(MedianTrackerRedux m, int start, int number){
		for (int i = 0; i < number; i ++){
			m.put3(start-i);
		}
	}
	
	public static long timeElapsed(long startTime, long currentTime){
		return currentTime - startTime;
	}
	
	public static float calcSpeed(long timeElapsed, long operationsCompleted){
		if (timeElapsed <= 0){
			return 0;
		}
		return (float) operationsCompleted / timeElapsed;
	}
	
	public static long operationsRemaining(long operationsLimit, long currentOperation){
		return operationsLimit - currentOperation;
	}
	
	public static float timeRemaining(long remaining, float speed){
		if (speed <= 0){
			return 0;
		}
		return (float)(remaining/speed);
	}
		
	public static void statusReport(long startTime, long currentTime, long currentOperation, long operationLimit){
		long elapsed = timeElapsed(startTime, currentTime);
		float newSpeed = calcSpeed(elapsed, currentOperation);
		long remaining = operationsRemaining(operationLimit, currentOperation);
		float timeRemaining = timeRemaining(remaining, newSpeed);
		System.out.println("-------------------------------------------------------------------");
		System.out.println("Operation: " + currentOperation);
		System.out.println("Time: " + currentTime);
		System.out.println("Elapsed: " + (int)elapsed/(1000*60) + " minutes");
		System.out.println("Operations remaining: " + remaining);
		System.out.println("peed: " + (long)newSpeed *1000 + " ops/second");
		System.out.println("Est. time remaining: " + (int)timeRemaining/(1000*60) + " minutes.");
		System.out.println("====================================================================");
	}
	
	public static String statusReportLine(long startTime, long currentTime, long currentOperation, long operationLimit){
		long elapsed = timeElapsed(startTime, currentTime);
		float newSpeed = calcSpeed(elapsed, currentOperation);
		long remaining = operationsRemaining(operationLimit, currentOperation);
		float timeRemaining = timeRemaining(remaining, newSpeed);
		return currentOperation + ", " + currentTime + ", " + elapsed/(1000*60) + ", " + remaining + ", " + newSpeed + ", " + timeRemaining/(1000*60);
	}
	
	
}
