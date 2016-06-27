package HashMap;

import java.util.List;

public interface HashMap2 {
	public int size();

	public boolean isEmpty();

	public Object get(Object key);

	public void put(Object key, Object value);

	public Object remove(Object key);

	public List<Object> keys();

	public List<Object> values();
}