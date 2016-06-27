package Set;

import java.util.List;

public class HashSet implements Set{
	
	private HashMap map;

	public HashSet(int size){
		
		map =  new SimpleHashMap(size);
	}
	@Override
	public int size() {
		
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		
		return map.isEmpty();
	}

	@Override
	public void add(Object o) {
		
		map.put(o, true);
	}

	@Override
	public void remove(Object o) {
		
		map.remove(o);
	}

	@Override
	public boolean contains(Object o) {
		
		return map.get(o) != null;
	}

	@Override
	public List<Object> getMembers() {
		
		return map.keys();
	}

	@Override
	public void union(Set s) {
		
		List<Object> newMembers = s.getMembers();
		
		for(Object member : newMembers){
			
			add(member);
			
		}
	}

	@Override
	public void intersection(Set s) {
		
	}

	@Override
	public void difference(Set s) {
		// TODO Auto-generated method stub
		
	}

}
