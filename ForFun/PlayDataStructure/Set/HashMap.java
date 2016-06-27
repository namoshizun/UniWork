package Set;

import java.util.List;

public interface HashMap<K, V> {
	
	public int size();

	public boolean isEmpty();

	public V get(K key);

	public void put(K key, V value);

	public V remove(K key);

	public List<K> keys();

	public List<V> values();
	
}