package HashMap;

import java.util.ArrayList;
import java.util.List;

class LinearHashMapEntry {
	public Object key;
	public Object value;

	public LinearHashMapEntry(Object key, Object value) {
		this.key = key;
		this.value = value;
	}
}

public class LinearHashMap implements HashMap2 {

	private LinearHashMapEntry[] items;
	private int noOfItems;

	public LinearHashMap(int size) {

		this.items = new LinearHashMapEntry[size];

	}

	public LinearHashMapEntry[] getItems() {
		return items;
	}

	private int hash(Object key) {

		return Math.abs(key.hashCode());

	}

	@Override
	public int size() {

		return noOfItems;
	}

	@Override
	public boolean isEmpty() {

		return noOfItems == 0;
	}

	@Override
	/*
	 * If get() sees that there is an element at the requested index, but it
	 * does not have the same key as the one requested, traverse the array from
	 * this position onwards looking for the required key.
	 * 
	 * If you reach the end of the array, go back to the beginning. If you reach
	 * the index you started at, or an empty cell, return null. Note that you
	 * should continue to search past defunct nodes, which leave a trace that
	 * the cell has been non-empty in its history.(non-Javadoc)
	 */
	public Object get(Object key) {

		int index = hash(key) % items.length;
		LinearHashMapEntry entry = items[index];
		boolean hasFound = false;

		if (!entry.key.equals(key)) {
			
			while (!hasFound) {
				++index;
				
				if (index == items.length) {
					index = 0;
				} else if (index == hash(key) % items.length || items[index] == null) {
					return null;
				} else if (items[index].key.equals(key)) {
					hasFound = true;
					entry = items[index];
				}
			}
		}
		return entry.value;
	}

	@Override
	public void put(Object key, Object value) {

		int index = hash(key) % items.length;

		if (noOfItems == items.length) {

			int length = 2 * items.length;
			LinearHashMapEntry[] expandedMap = new LinearHashMapEntry[length];

			// Copy the old items into a new longer item list.
			for (int i = 0; i < items.length; ++i) {
				int newIndex = hash(items[i].key) % length;
				expandedMap[newIndex] = items[i];
			}
			items = expandedMap;

			// Put the current object into new items array.
			index = hash(key) % length;
			items[index] = new LinearHashMapEntry(key, value);

		} else if (items[index] == null) {
			items[index] = new LinearHashMapEntry(key, value);
		} else if (!items[index].key.equals(key)) {
			boolean findFree = false;
			
			while (!findFree) {
				++index;
				if (index == items.length) {
					index = 0;
				}
				if (items[index] == null) {
					items[index] = new LinearHashMapEntry(key, value);
					findFree = true;
				}
			}
		}
		++noOfItems;
	}

	@Override
	public Object remove(Object key) {

		int index = hash(key) % items.length;
		LinearHashMapEntry entry = items[index];
		
		if (entry != null) {
			--noOfItems;
			items[index] = null;
		}
		return entry;
	}

	@Override
	public List<Object> keys() {

		List<Object> keys = new ArrayList<Object>();

		for (LinearHashMapEntry entry : items) {
			if (entry != null) {
				keys.add(entry.key);
			}
		}
		return keys;
	}

	@Override
	public List<Object> values() {

		List<Object> values = new ArrayList<Object>();

		for (LinearHashMapEntry entry : items) {
			if (entry != null) {
				values.add(entry.value);
			}
		}
		return values;
	}
}
