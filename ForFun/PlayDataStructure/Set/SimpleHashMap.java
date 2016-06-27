package Set;

import java.util.ArrayList;
import java.util.List;

class HashMapEntry {
	public Object key;
	public Object value;

	public HashMapEntry(Object key, Object value) {
		this.key = key;
		this.value = value;
	}
}

public class SimpleHashMap implements HashMap {

	private HashMapEntry[] items;
	private int noOfItems;

	public HashMapEntry[] getItems() {
		return items;
	}

	private int hash(Object key) {

		return Math.abs(key.hashCode());

	}

	public SimpleHashMap(int size) {

		this.items = new HashMapEntry[size];

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
	public Object get(Object key) {

		int index = hash(key) % items.length;
		HashMapEntry entry = items[index];

		if (entry == null) {
			return null;
		} else if (!entry.key.equals(key)) {
			return null;
		} else {
			return entry.value;

		}
	}

	@Override
	public void put(Object key, Object value) {

		int index = hash(key) % items.length;
		
		if (noOfItems == items.length) {

			int length = 2 * items.length;
			HashMapEntry[] expandedMap = new HashMapEntry[length];

			// Copy the old items into a new longer item list.
			for (int i = 0; i < items.length; ++i) {

				index = hash(items[i].key) % length;
				expandedMap[index] = items[i];
			}
			items = expandedMap;

			// Put the current object into new items array.
			index = hash(key) % length;
			items[index] = new HashMapEntry(key, value);

		} else if (items[index] == null){

			items[index] = new HashMapEntry(key, value);
			
		} else{
			
			boolean findFree = false;

			while (!findFree) {

				++index;

				if (index == items.length) {
					index = 0;
				}

				if (items[index] == null) {
					items[index] = new HashMapEntry(key, value);
					findFree = true;
				}
			}
		}
		
		++noOfItems;
	}

	@Override
	public Object remove(Object key) {

		int index = hash(key) % items.length;
		HashMapEntry entry = items[index];

		if (entry != null) {
			--noOfItems;
			items[index] = null;
		}

		return entry;
	}

	@Override
	public List<Object> keys() {

		List<Object> keys = new ArrayList<Object>();

		for (HashMapEntry entry : items) {

			if (entry != null) {
				keys.add(entry.key);
			}
		}

		return keys;
	}

	@Override
	public List<Object> values() {

		List<Object> values = new ArrayList<Object>();

		for (HashMapEntry entry : items) {

			if (entry != null) {
				values.add(entry.value);
			}
			
		}

		return values;
	}

}
