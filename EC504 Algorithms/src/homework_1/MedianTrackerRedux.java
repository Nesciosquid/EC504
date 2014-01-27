package homework_1;

import java.util.ArrayList;
import java.util.Collections;

public class MedianTrackerRedux {
	ArrayList<Long> values;
	int medIndexLeft = -1;
	int medIndexRight = 0;
	boolean droppingRight = false;
	boolean droppingLeft = false;
	long maxLost;
	long minLost;
	long count;
	int maxSize = 1000000;
	boolean verbose = false;

	public MedianTrackerRedux() {
		values = new ArrayList<Long>();
	}

	public MedianTrackerRedux(int size) {
		maxSize = size;
		values = new ArrayList<Long>(maxSize);
	}

	public void setVerboseMode(boolean state) {
		verbose = state;
	}

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

	private int searchIndex(long newVal) {
		int pos = Collections.binarySearch(values, newVal);
		if (pos < 0) {
			return -(pos + 1);
		} else {
			return pos;
		}
	}

	private int getLeftMiddleIndex() {
		if (values.size() % 2 == 0) {
			return (int) values.size() / 2 - 1;
		}
		return (int) Math.floor(values.size() / 2);
	}

	private int getRightMiddleIndex() {
		if (values.size() % 2 == 0) {
			return (int) values.size() / 2 + 1;
		}
		return (int) Math.floor(values.size() / 2);
	}

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

	/* Clean up this conditional statement! */
	public float put3(long newVal) {
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
			if (values.size() >= maxSize){
				if (medianOffset() <= 0){
					dropRight();
				}
				else {
					dropLeft();
				}
			}
		}
		return getMedian();
	}

	/* Clean up this conditional statement! */
	public float put2(long newVal) {
		int newIndex = searchIndex(newVal);
		if (!listTooBig()) { // list has not yet reached maximum size
			addValue(newVal, newIndex);
			return getMedian();
		} else {
			int safety = safeToAdd(newVal);
			if (safety == 1) { // new value is larger than max value previously
								// thrown away
				shiftMedRight();
			} else if (safety == -1) { // new value is smaller than min value
										// previously thrown away
				shiftMedLeft();
			} else {
				int off = medianOffset();
				int currentSize = values.size();
				if (newIndex == currentSize && off <= 0) {
					maxLost = newVal;
					droppingRight = true;
					shiftMedRight();
				} else if (newIndex == currentSize - 1 && off <= 0) {
					maxLost = values.get(values.size() - 1);
					values.set(values.size() - 1, newVal);
					shiftMedRight();
					droppingRight = true;
				} else if (newIndex == 1 && off >= 0) {
					minLost = values.get(0);
					values.set(0, newVal);
					droppingLeft = true;
					shiftMedLeft();
				} else if (newIndex == 0 && off >= 0) {
					minLost = newVal;
					shiftMedLeft();
					droppingLeft = true;
				} else {
					addValue(newVal, newIndex);
					shiftMedRight();
					if (medianOffset() <= 0) {
						dropRight();
					} else {
						dropLeft();
					}
				}
			}
			return getMedian();

		}
	}

	/* Clean up this conditional statement! */
	public float put(long newVal) {
		if (!listTooBig()) { // list has not yet reached maximum size
			int newIndex = searchIndex(newVal);
			addValue(newVal, newIndex);
			return getMedian();
		} else {
			int safety = safeToAdd(newVal);
			if (safety == 1) { // new value is larger than max value previously
								// thrown away
				shiftMedRight();
			} else if (safety == -1) { // new value is smaller than min value
										// previously thrown away
				shiftMedLeft();
			} else {
				int newIndex = searchIndex(newVal);
				int off = medianOffset();
				if (off == 0) {
					if (newIndex == values.size()) {
						shiftMedRight();
						droppingRight = true;
					} else if (newIndex == values.size() - 1) {
						values.set(values.size() - 1, newVal);
						shiftMedRight();
						droppingRight = true;
					} else if (newIndex == 1) {
						values.set(0, newVal);
						droppingLeft = true;
						shiftMedLeft();
					} else if (newIndex == 0) {
						shiftMedLeft();
						droppingLeft = true;
					} else {
						addValue(newVal, newIndex);
						assert (medianOffset() != 0); // oh snap!
						if (medianOffset() == 1) {
							dropLeft();
						} else {
							dropRight();
						}
					}
				} else if (off == 1) {
					if (newIndex == 1) {
						values.set(0, newVal);
						shiftMedLeft();
						droppingLeft = true;
					} else if (newIndex == 0) {
						shiftMedLeft();
						droppingLeft = true;
					} else {
						addValue(newVal, newIndex);
						dropLeft();
					}
				} else {
					if (newIndex == values.size()) {
						shiftMedRight();
						droppingRight = true;
					} else if (newIndex == values.size() - 1) {
						values.set(values.size() - 1, newVal);
						shiftMedRight();
						droppingRight = true;
					} else {
						addValue(newVal, newIndex);
						dropRight();
					}
				}

			}
			return getMedian();

		}
	}

	public void addValue(long newVal, int index) {
		if (index == values.size()) {
			values.add(newVal);
		} else {
			values.add(index, newVal);
		}
		shiftMedRight();
	}

	public float getMedian() {
		if (medIndexLeft < 0 || medIndexRight > values.size() - 1) {
			System.out.println("YOU ARE SO FUCKED!");
			return -1;
		} else {
			if (medIndexLeft == medIndexRight) {
				return values.get(medIndexLeft);
			} else
				return (values.get(medIndexLeft) + values.get(medIndexRight)) / 2f;
		}
	}

	private void shiftMedLeft() {
		if (medIndexLeft == medIndexRight) {
			medIndexLeft--;
		} else {
			medIndexRight = medIndexLeft;
		}
	}

	private void shiftMedRight() {
		if (medIndexRight == medIndexLeft) {
			medIndexRight++;
		} else {
			medIndexLeft = medIndexRight;
		}
	}

	private int safeToAdd(long newVal) {
		if (droppingRight && newVal > maxLost) {
			return 1;
		} else if (droppingLeft && newVal < minLost) {
			return -1;
		} else {
			return 0;
		}
	}

	private void dropLeft() {
		if (!droppingLeft) {
			droppingLeft = true;
		}
		minLost = values.get(0);
		values.remove(0);
		shiftMedLeft();
		shiftMedLeft();
	}

	private void dropRight() {
		if (!droppingRight) {
			droppingRight = true;
		}
		maxLost = values.get(values.size() - 1);
		values.remove(values.size() - 1);
	}

}
