import java.util.*;

public class Entry {
	// A LinkedList of generic object.
	private List<Object> values;
	private Object key;

	public Entry(Object key, List<Object> values) {
		this.key = key;
		this.values = values;
	}
	
	public Object getKey(){
		return this.key;
	}
	
	public List<Object> getValues(){
		return this.values;
	}
	
	public void setValues(LinkedList<Object> values){
		this.values = values;
	}
	
	public void reverseValues(){
		List<Object> reversed = new LinkedList<Object>();
		ListIterator<Object> itr = reversed.listIterator(values.size());
		while(itr.hasPrevious()){
			reversed.add(itr.previous());
		}
		this.values = reversed;
	}
	
}
