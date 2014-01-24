package homework_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RunningMedian {
	//long[] values = new long[1];
	ArrayList<Long> values = new ArrayList<>();
	int count = 0;
	boolean VERBOSE_MODE = false;
	float lastMedian = 0;

	RunningMedian() {
	}

	RunningMedian(boolean verbose) {
		if (verbose) {
			VERBOSE_MODE = true;
		}
	}
	
	public void setVerbose(boolean enable){
		VERBOSE_MODE = enable;
	}

	float getMedian() {
		return lastMedian;
	}

	float calcMedian(long[] allValues) {
		int targetIndex = getMiddle();
		if (!isEven()) {
			lastMedian = allValues[targetIndex];
			return lastMedian;
		} else {
			lastMedian =  (float) (allValues[targetIndex] + allValues[targetIndex + 1]) / 2;
			return lastMedian;
		}
	}
	
	float calcMedian(ArrayList<Long> valueList){
		int targetIndex = getMiddle();
		if (!isEven()){
			lastMedian = valueList.get(targetIndex);
			return lastMedian;
		}
		else {
			lastMedian = (float) (valueList.get(targetIndex) + valueList.get(targetIndex + 1)) / 2;
			return lastMedian;
		}
	}

	boolean isLargest(long newVal, long[] allValues) {
		if (newVal >= allValues[count - 1]) {
			return true;
		} else {
			return false;
		}
	}
	
	boolean isLargest(long newVal, ArrayList<Long> valueList) {
		if (newVal >= valueList.get(valueList.size() -1)) {
			return true;
		} else {
			return false;
		}
	}

	boolean isSmallest(long newVal, long[] allValues) {
		if (newVal <= allValues[0]) {
			return true;
		} else {
			return false;
		}
	}
	
	boolean isSmallest(long newVal, ArrayList<Long> valueList) {
		if (newVal <= valueList.get(0)) {
			return true;
		} else {
			return false;
		}
	}

	long[] addValueAtPosition(int index, long newVal, long[] allValues) {
		long[] temp = new long[count + 1];
		for (int i = 0; i < index; i++) {
			temp[i] = allValues[i];
		}
		temp[index] = newVal;
		for (int i = index + 1; i < count; i++) {
			temp[i] = allValues[i - 1];
		}

		return temp;
	}

	long[] placeValueBinarySearch(long newVal, long[] allValues) {
		if (VERBOSE_MODE) {
			if (count % 1000 == 0) {
				System.out.println("Placing value: " + count + 1);
			}
		}
		int newPosition = 0;
		if (count == 0) {
			allValues[newPosition] = newVal;
		} else {
			newPosition = (count + 1 + Arrays.binarySearch(allValues, newVal));
		}
		return addValueAtPosition(newPosition, newVal, allValues);
	}
	
	void placeValueBinarySearch(long newVal, ArrayList<Long> valueList){
		if (VERBOSE_MODE) {
			if (count % 1000 == 0) {
				System.out.println("Placing value: " + (count + 1));
			}
		}
		int newPosition = 0;
		if (count == 0){
			valueList.add(newVal);
		}
		else if (isLargest(newVal, valueList))
		{
			valueList.add(newVal);
		}
		else if (isSmallest(newVal, valueList)){
			valueList.add(0,newVal);
		}
		else {
			int pos = Collections.binarySearch(valueList, newVal);
			if (pos < 0){
				newPosition = -(pos+1);
			}
			else {
				newPosition = pos;
			}
			valueList.add(newPosition,newVal);
		}
	}

	void doubleArray(long allValues[]) {
		allValues = Arrays.copyOf(allValues, allValues.length * 2);
	}

	public int getMiddle() {
		return (int) ((double) Math.floor(count - 1) / 2);
	}

	public boolean isEven() {
		if (count == 0) {
			return false;
		} else if (count % 2 == 0) {
			return true;
		} else
			return false;
	}

	public boolean isFull(long[] allValues) {
		if (count == allValues.length - 1) {
			return true;
		} else {
			return false;
		}
	}

	public long peek() {
		return values.get(count-1);
	}
	

	public long getSize() {
		return values.size();
	}

	public int getCount() {
		return count;
	}

	public float put(long newVal) {
		placeValueBinarySearch(newVal, values);
		count++;
		calcMedian(values);
		return getMedian();
	}


	public void printValues(long[] allValues) {
		for (int i = 0; i < allValues.length; i++) {
			System.out.print(allValues[i] + ", ");
		}
		System.out.println("");
	}
	
	public void printValues(ArrayList<Long> valueList){
		for (int i = 0; i < valueList.size(); i++){
			System.out.print(valueList.get(i) + ", ");
		}
		System.out.println("");
	}

	public void put(long newVal, boolean verbose) {
		if (verbose) {
			put(newVal);
		} else
			put(newVal);
	}

}
