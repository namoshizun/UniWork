package Set;

import java.util.List;

public interface Set {
	public int size();

	public boolean isEmpty();

	public void add(Object o);

	public void remove(Object o);

	// 'o' existes in the set if its value is TRUE.
	public boolean contains(Object o);

	public List<Object> getMembers();

	public void union(Set s);

	public void intersection(Set s);

	// This is implemented by in-place (eg, s1.difference(s2)).
	public void difference(Set s);
}