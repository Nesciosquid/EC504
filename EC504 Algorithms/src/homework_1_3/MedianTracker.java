package homework_1_3;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class allows running median calculations for datasets of known size and
 * complexity.
 * 
 * It adds incoming values into a sorted ArrayList up to a maximum size, which
 * can be set using the constructor. Once the maximum size is reached, it will
 * selectively drop values from the ArrayList to try and keep the median in the
 * center of the list. Decreasing the maxSize will allow sets to be calculated
 * more rapidly, but will increase the likelihood of failure.
 * 
 * New values may not be added to a full list if they would fall outside of the
 * bounds of a value that was previously thrown away. In this case, the new
 * value is thrown away, and the median indices are adjusted accordingly. If
 * this happens to many times, the median indices will eventually reach the ends
 * of the array, and an error will be reported.
 * 
 * The index where the median value is stored is tracked by two indices,
 * medIndexLeft and medIndexRight, which are shifted left or right upon
 * modifications to the list.
 * 
 * @author Aaron Heuckroth
 * 
 */
public class MedianTracker {

	/*
	 * The current list of values stored. Does not contain values that have been
	 * dropped to preserve maximum list size. *
	 */
	ArrayList<Long> values;

	/**
	 * The index of the "left" median value. If the list has an odd number of
	 * elements, this should be the same index as the "right" median value.
	 * 
	 * Starts at -1 because adding the first value to the list will shift the
	 * median to the right, which will result in medIndexLeft being incremented
	 * to 0.
	 */
	int medIndexLeft = -1;

	/**
	 * The index of the "right" median value. If the list has an odd number of
	 * elements, this should be the same index as the "left" median value.
	 */
	int medIndexRight = 0;

	/*
	 * The booleans droppingLeft and droppingRight are set to true once the
	 * maximum list size has been reached and values are now being thrown away
	 * in an attempt to keep the list small.
	 */
	boolean droppingRight = false;
	boolean droppingLeft = false;

	/*
	 * The isBroken flag is set when either median index falls outside the
	 * bounds of the list, indicating that the correct median can no longer be
	 * caculated for the given set.
	 */
	boolean isBroken = false;

	/*
	 * The values minLostRight and maxLostLeft represent cutoffs for adding new
	 * values to the list. They represent the greatest value dropped off of the
	 * left (low) side of the list, and the lowest value dropped off of the
	 * right (high) side of the list. New values outside of these cutoffs will
	 * be dropped automatically without insertion into the list.
	 */
	long minLostRight;
	long maxLostLeft;

	/*
	 * The total number of values added to the list so far, including those
	 * which have been dropped.
	 */
	long count;

	/*
	 * This default maximum size was selected to allow most data sets to be run
	 * using the default constructor. It should be noted that calculation speed
	 * decreases with increases in maxSize, due to the time required to perform
	 * inserts on large ArrayLists.
	 * 
	 * For ideal run times, choose a maxSize that is as low as possible, but
	 * greater than half of the number of unique medians required to be stored
	 * for your dataset.
	 * 
	 * For example: The sequence 1 -> 10000 would run without errors with a
	 * maxSize of 6000, since you need only store approximately 5000 unique
	 * medians to ensure the answer is still accurate. A randomly assorted
	 * sequence of the numbers 1 -> 10000 could be computed with a significantly
	 * lower maxSize, since values are expected to fall on either side of the
	 * true median.
	 * 
	 * When in doubt: Start with a small-but-reasonable maxSize, and increase
	 * until the set can finish without errors.
	 * 
	 * A new MedianTracker should be constructed for each set.
	 */
	int maxSize = 1000000;

	/*
	 * Enables some methods to output more information to standard.out when
	 * called.
	 */
	boolean verbose = false;

	public MedianTracker() {
		values = new ArrayList<Long>();
	}

	/**
	 * Creates a new MedianTracker that can store up to the specified number of
	 * values. For best results, use the smallest size that can store all the
	 * unique medians that will be computed for a given set. For sequential
	 * sets, choose a size greater than half of the size of the set.
	 */
	public MedianTracker(int size) {
		maxSize = size;
		values = new ArrayList<Long>(maxSize);
	}

	public long getCount() {
		return count;
	}

	public long getSize() {
		return values.size();
	}

	public long getMaxSize() {
		return maxSize;
	}

	/** Enables verbose state reporting, useful for testing purposes. */
	public void setVerboseMode(boolean state) {
		verbose = state;
	}

	/*
	 * Rarely used, since new MedianTrackers should be constructed with the
	 * desired size for each set.
	 */
	public void setSize(int newSize) {
		maxSize = newSize;
	}

	private boolean listTooBig() {
		if (values.size() >= maxSize) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Uses the Collections binarySearch method to find the proper position to
	 * insert a new value into the list. Negative values are converted to
	 * positive indexes.
	 */
	private int searchIndex(long newVal) {
		int pos = Collections.binarySearch(values, newVal);
		if (pos < 0) {
			return -(pos + 1);
		} else {
			return pos;
		}
	}

	/**
	 * Gets the "leftmost middle index", for determining whether the median is
	 * centered. Should be equal to getRightMiddleIndex() if the list has an odd
	 * number of elements.
	 */
	private int getLeftMiddleIndex() {
		if (values.size() % 2 == 0) {
			return (int) values.size() / 2 - 1;
		}
		return (int) Math.floor(values.size() / 2);
	}

	/**
	 * Gets the "rightmost middle index", for determining whether the median is
	 * centered. Should be equal to getLeftMiddleIndex() if the list has an odd
	 * number of elements.
	 */
	private int getRightMiddleIndex() {
		if (values.size() % 2 == 0) {
			return (int) values.size() / 2 + 1;
		}
		return (int) Math.floor(values.size() / 2);
	}

	/**
	 * Returns an integer representing the offset of the median position from
	 * the center of the list. If the median is too far 'left', it returns -1.
	 * If the median is too far right, it returns 1. If the median is centered,
	 * it returns 0.
	 * 
	 * @return
	 */
	private int medianOffset() {
		if (medIndexLeft < getLeftMiddleIndex()
				|| medIndexRight < getRightMiddleIndex()) {
			return -1;
		} else if (medIndexLeft > getLeftMiddleIndex()
				|| medIndexRight > getRightMiddleIndex()) {
			return 1;
		} else {
			return 0;
		}

	}

	/**
	 * Determines whether a new value is safe to add to the list, and processes
	 * it. If the value is safe to add, it adds it, checks to see if the list is
	 * made too large by the addition, and drops a value if necessary. If not,
	 * the value is ignored and the median indices are shifted as needed.
	 */
	public float put(long newVal) {
		// list has not yet reached maximum size
		int safety = safeToAdd(newVal);
		if (safety == 1) { // new value is larger than max value previously
							// thrown away
			shiftMedRight();
		} else if (safety == -1) { // new value is smaller than min value
									// previously thrown away
			shiftMedLeft();
		} else {
			int newIndex = searchIndex(newVal);
			addValue(newVal, newIndex);
			if (listTooBig()) {
				if (medianOffset() <= 0) {
					dropRight();
				} else {
					dropLeft();
				}
			}
		}
		count++;
		return getMedian();
	}

	/**
	 * Add a new value to the list at the specified index. If the value belongs
	 * at the end of the list, simply add it to the list. This makes this method
	 * much faster for sequentially increasing datasets.
	 */
	public void addValue(long newVal, int index) {
		if (index == values.size()) {
			values.add(newVal);
		} else {
			values.add(index, newVal);
		}
		shiftMedRight();
	}

	/**
	 * Return the median, as determined by the values at the left and right
	 * median indices, averaging as necessary.
	 * 
	 * If the median cannot be calculated reliably, it prints an error and
	 * ensures that errors will be thrown at all subsequent calls.
	 */
	public Float getMedian() {
		if (medIndexLeft < 0 || medIndexRight > values.size() - 1
				|| isBroken == true) {
			System.out
					.println("Error in running median calculation. Answer is no longer accurate.");
			System.out
					.println("Please re-run your dataset with a larger maximum size.");
			isBroken = true;
			return -1f;
		} else {
			if (medIndexLeft == medIndexRight) {
				return (float) values.get(medIndexLeft);
			} else
				return (values.get(medIndexLeft) + values.get(medIndexRight)) / 2f;
		}
	}

	public Long getMedianLeft() {
		return values.get(medIndexLeft);
	}

	public Long getMedianRight() {
		return values.get(medIndexRight);
	}

	/**
	 * Shifts the median to the left, handling movement of the left and right
	 * indices.
	 * 
	 */
	private void shiftMedLeft() {
		if (medIndexLeft == medIndexRight) {
			medIndexLeft--;
		} else {
			medIndexRight = medIndexLeft;
		}
	}

	/**
	 * Shifts the median to teh right, handling movement of the left and right
	 * indices.
	 * 
	 */
	private void shiftMedRight() {
		if (medIndexRight == medIndexLeft) {
			medIndexRight++;
		} else {
			medIndexLeft = medIndexRight;
		}
	}

	/**
	 * Determines whether a new value is safe to add, based on numbers
	 * previously dropped from the list.
	 * 
	 */
	private int safeToAdd(long newVal) {
		if (droppingRight && newVal > minLostRight) {
			return 1;
		} else if (droppingLeft && newVal < maxLostLeft) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * Removes the first value from the list and shifts the median left twice,
	 * since the positions of all elements in the list will shift left by one
	 * after the removal.
	 */
	private void dropLeft() {
		if (!droppingLeft) {
			droppingLeft = true;
		}
		maxLostLeft = values.get(0);
		values.remove(0);
		shiftMedLeft();
		shiftMedLeft();
	}

	/**
	 * Removes the last value of the list. The median indices do not need to be
	 * shifted, since the indices of all other list elements do not change.
	 * 
	 */
	private void dropRight() {
		if (!droppingRight) {
			droppingRight = true;
		}
		minLostRight = values.get(values.size() - 1);
		values.remove(values.size() - 1);
	}

}
