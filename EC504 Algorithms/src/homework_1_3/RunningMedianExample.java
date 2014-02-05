package homework_1_3;

public class RunningMedianExample {

	/*
	 * The value sent to the MedianTracker constructor represents the total
	 * number of unique medians expected to be stored for a given sequence, plus
	 * some headroom.
	 * Since the example sequence has only 15 values in it, we
	 * know that this will be large enough to compute an accurate median without
	 * needing to store all values.
	 */
	private static MedianTracker med = new MedianTracker(10);

	public static void main(String[] args) {
		int limit = 15;
		Float currentMedian;
		for (long i = 0; i < limit; i++) {
			currentMedian = runningMedian(i);
			System.out.println("Added value: " + i + ", current median is: " + currentMedian);
		}
	}

	/**
	 * This is just a wrapper method around the MedianTracker class.
	 * 
	 * @param value
	 *            -- The long value to be added to the list of existing previous
	 *            values.
	 * @return -- The median (as a Float) of the list of values, including the
	 *         values passed to all previous calls of runningMedian();
	 */
	public static Float runningMedian(Long value) {
		return med.put(value);
	}

}
