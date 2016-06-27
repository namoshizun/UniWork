package LeetCode;
import java.util.*;
/**
 * Given two strings s and t, determine if they are isomorphic.
 * 
 * Two strings are isomorphic if the characters in s can be replaced to get t.
 * 
 * All occurrences of a character must be replaced with another character while
 * preserving the order of characters. No two characters may map to the same
 * character but a character may map to itself.
 * 
 * For example, Given "egg", "add", return true.
 * 
 * Given "foo", "bar", return false.
 * Given "paper", "title", return true.
 */

public class Isomorphic_String {
	
	public boolean isIsomorphic(String s, String t){
		if(s.length() != t.length())
			return false;
		
		Map<Character, Character> map = new HashMap<Character, Character>();
		for(int i = 0; i < s.length(); ++i){
			char key = s.charAt(i);
			char value = t.charAt(i);
			if(map.get(key) == null)
				// Enforce one to one mapping
				if(map.containsValue(value))
					return false;
				else
					map.put(key, value);
			else if (!map.get(key).equals(value))
				return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		Isomorphic_String player = new Isomorphic_String();
		String s1 = "ab";
		String t1 = "aar";
		String s2 = "paper";
		String t2 = "title";
		System.out.println(player.isIsomorphic(s1, t1));
		System.out.println(player.isIsomorphic(s2, t2));
	}

}
