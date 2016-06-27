package EulerP;

import java.util.*;

import javax.management.Query;

/**
 * Largest palindrome product
 * Problem 4
 * 
 * A palindromic number reads the same both ways. The largest palindrome made from the product of 
 * two 2-digit numbers is 9009 = 91 * 99.
 * 
 * Find the largest palindrome made from the product of two 3-digit numbers.
 */

public class P4 {

	public static void main(String[] args) {
		
		P4 tester = new P4();
		System.out.println(isPalindromic(199991));
		
		List<Integer> n1 = new ArrayList<Integer>() ;
		List<Integer> n2 = new ArrayList<Integer>();
		for(int i = 1000; i >= 100; --i){
			n1.add(i);
			n2.add(i);
		}
		
		int max = 0;
		for(Integer number1 : n1){
			for(Integer number2 : n2){
				int testN = number1 * number2;
				if(isPalindromic(testN)){
					if(testN > max){
						max = testN;
					}
				}
			}
		}
		System.out.println(max);
	}
	
	public static boolean isPalindromic (int n ){
		
		Stack<Integer> front = new Stack<Integer>();
		Queue<Integer> back = new LinkedList<Integer>();

		for(Character c : Integer.toString(n).toCharArray()){
			front.push((int) c);
			back.add((int) c);
		}
		
		while(!front.isEmpty() && !back.isEmpty()){
			
			if(front.pop() != back.poll()){
				return false;
			}
		}
		return true;
	}
}
