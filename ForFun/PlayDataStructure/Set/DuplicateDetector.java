package Set;

import java.util.ArrayList;
import java.util.List;

public class DuplicateDetector {
    /**
     * Returns a new list with all items from l (but with duplicate items removed)
     * The order of elements may change.
     */
    public static List<Object> removeDuplicates(List<Object> l){
    	
    	List<Object> newList = new ArrayList<Object>();
    	HashMap helper = new SimpleHashMap(10);
    	
    	if(l.isEmpty()){
    		
    		return l;
    		
    	} else {
    		
    		for(Object o : l){
    			helper.put(o, true);
    		}
    		for(Object o : helper.keys()){
    			newList.add(o);
    		}
    		
    		return newList;
    	}
    	
    }
    
    public static void main (String[] agrs){
    	
    	List<Object> ha = new ArrayList<Object>();
    	ha.add("yoo");
    	ha.add("eh");
    	ha.add("a");
    	ha.add("a");
    	
    	for(Object hey : removeDuplicates(ha)){
    		System.out.println(hey);
    	}
    }
}
