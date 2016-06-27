package util;

public class TypedPriorityNode<K extends Comparable<K>, V> implements Comparable<TypedPriorityNode> {

	public K key;
	public V value;

	public TypedPriorityNode(K key, V value) {

		this.key = key;
		this.value = value;
	}

	@Override
	public int compareTo(TypedPriorityNode node) {

		return this.key.compareTo((K) node.key);
	}
}