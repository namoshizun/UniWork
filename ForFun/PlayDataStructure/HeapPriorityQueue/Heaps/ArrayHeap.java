package Heaps;

import java.util.ArrayList;
import java.util.List;

class HeapNode {

	public int key;
	public Object value;

	public HeapNode(int key, Object value) {

		this.key = key;
		this.value = value;
	}
}

public class ArrayHeap implements Heap {

	private List<HeapNode> tree;

	public ArrayHeap() {

		this.tree = new ArrayList<HeapNode>();

	}

	/** Helper methods to make navigating arrat easier */
	private int getParentIndex(int index) {

		return (index - 1) / 2;
	}

	private int getLeftChildIndex(int index) {

		return 2 * index + 1;
	}

	private int getRightChildIndex(int index) {

		return 2 * index + 2;
	}

	private void swap(int currentIndex, int parentIndex) {

		HeapNode temp = tree.get(currentIndex);
		tree.set(currentIndex, tree.get(parentIndex));
		tree.set(parentIndex, temp);
	}

	/** Above are helper methods */

	@Override
	public void insert(int key, Object value) {

		tree.add(new HeapNode(key, value));

		int currentIndex = tree.size() - 1;
		while (currentIndex != 0) {
			int parentIndex = getParentIndex(currentIndex);
			if (tree.get(currentIndex).key > tree.get(parentIndex).key) {
				// Tree is valid, do nothing;
				return;
			} else {
				// Swap node with its parent and continue checking from the new
				// node's position.
				swap(currentIndex, parentIndex);
				currentIndex = parentIndex;
			}
		}
		return;
	}

	@Override
	public Object removeRoot() {

		if (tree.size() == 1) {

			return tree.remove(0).value;
		}

		// First, start finding the last node in the tree and replacing the
		// root with it.

		Object oldRootValue = tree.get(0).value;
		HeapNode lastNode = tree.remove(tree.size() - 1);
		tree.set(0, lastNode);

		int currentIndex = 0;
		while (true) {
			int leftChildIndex = getLeftChildIndex(currentIndex);
			int rightChildIndex = getRightChildIndex(currentIndex);

			if (leftChildIndex >= tree.size() && rightChildIndex >= tree.size()) {

				return oldRootValue;
			} else {

				int smallestChildIndex;
				if (leftChildIndex < tree.size() && rightChildIndex < tree.size()) {
					// Both are children;
					if (tree.get(leftChildIndex).key < tree.get(rightChildIndex).key) {
						smallestChildIndex = leftChildIndex;
					} else {
						smallestChildIndex = rightChildIndex;
					}
				} else {
					// Left child only;
					smallestChildIndex = leftChildIndex;
				}

				if (tree.get(currentIndex).key < tree.get(smallestChildIndex).key) {

					return oldRootValue;
				} else {
					
					swap(currentIndex, smallestChildIndex);
					currentIndex = smallestChildIndex;
				}
			}
		}
	}

	@Override
	public int size() {

		return tree.size();
	}

	@Override
	public boolean isEmpty() {

		return tree.isEmpty();
	}
	
	public static void main (String [] args){
        Heap h = new ArrayHeap();
        h.insert(7, "A");
        h.insert(8, "B");
        h.insert(9, "D");
        h.insert(5, "E");
        h.insert(1, "C");
        h.insert(2, "v");
        h.insert(3, "r");
        
        System.out.println(h.removeRoot());
	}
}
