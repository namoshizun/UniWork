package LeetCode;
import java.util.*;

/**
 * - https://leetcode.com/problems/valid-anagram/
 * Given two strings s and t, write a function to determine if t is an anagram of s.
 * 
 * For example
 * s = "anagram", t = "nagaram", return true.
 * s = "rat", t = "car", return false.
 * 
 * Note:
 * You may assume the string contains only lowercase alphabets.
 */

public class Valid_Anagram {

	public boolean isAnagram(String s, String t) {
		int[] alphabtCount = new int [26];
		int s_len = s.length();
		int t_len = t.length();
		
		if(s_len != t_len)
			return false;
		
		for(int i = 0; i < s_len; ++i) {
			// Convert char type to integer 0~25
			++alphabtCount[s.charAt(i) - 97];
			--alphabtCount[t.charAt(i) - 97];
		}
		
		for(int i = 0; i < 26; ++i) {
			if(alphabtCount[i] != 0) 
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Valid_Anagram run = new Valid_Anagram();
		System.out.println(run.isAnagram("rat", "cat"));
	}

}
