package util;

import java.util.ArrayList;
import java.util.Collections;


public class ArrayPriorityQueue implements PriorityQueue {

	private ArrayList<TypedPriorityNode> prioQueue;
	
	public ArrayPriorityQueue() {

		prioQueue = new ArrayList<TypedPriorityNode>();
	}

	// ----------------Helper methods below =-------
	public void sortQueue() {

		Collections.sort(prioQueue);
	}

	public ArrayList<TypedPriorityNode> getQueue() {

		return prioQueue;
	}
	// --------------=Helper methods above =----------
	@Override
	public void insert(Comparable key, Object value) {

		prioQueue.add(new TypedPriorityNode(key, value));
	}

	@Override
	public Object removeMin() {

		if (prioQueue.isEmpty()) {
			return null;
		} else {
			sortQueue();
			return prioQueue.remove(0).value;
		}
	}

	@Override
	public Object min() {

		if (prioQueue.isEmpty()) {
			return null;
		} else {
			sortQueue();
			return prioQueue.get(0).value;
		}
	}

	@Override
	public int size() {

		return prioQueue.size();
	}

	@Override
	public boolean isEmpty() {

		return prioQueue.isEmpty();
	}
	

}
