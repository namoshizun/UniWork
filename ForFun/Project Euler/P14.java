package EulerP;

public class P14 {

	/**
	 * Longest Collatz sequence
	 * 
	 * The following iterative sequence is defined for the set of positive
	 * integers: n -> n/2 (n is even) n-> 3n + 1 (n is odd)
	 * 
	 * Starting with a number N and apply to the rules above allows to generate
	 * a sequence. Although not yet been proved, it is thought that all the
	 * sequence ends at 1.
	 * 
	 * Which starting number, under one million, produces the longest chain?
	 * NOTE: Once the chain starts the terms are allowed to go above one
	 * million.
	 */

	// Direct Approach
	public static String run() {
		
		int number = 1000000;
		 
		long sequenceLength = 0;
		long startingNumber = 0;
		long sequence;
		 
		for (int i = 2; i <= number; i++) {
		    int length = 1;
		    sequence = i;
		    while (sequence != 1) {
		        if ((sequence % 2) == 0) {
		            sequence = sequence / 2;
		        } else {
		            sequence = sequence * 3 + 1;
		        }
		        length++;
		    }
		 
		    //Check if sequence is the best solution
		    if (length > sequenceLength) {
		        sequenceLength = length;
		        startingNumber = i;
		    }
		}
		return Long.toString(startingNumber);
	}
	
	// Faster Approach by using catching
	public static String run2(){
		
		int number = 1000000;
		 
		int sequenceLength = 0;
		int startingNumber = 0;
		long sequence;
		 
		// Cache stores the length of i^th number found already.
		int[] cache = new int[number + 1];
		//Initialise cache
		for (int i = 0; i < cache.length; i++) {
		    cache[i] = -1;
		}
		cache[1] = 1;
		 
		for (int i = 2; i <= number; i++) {
		    sequence = i;
		    int k = 0;
		    while (sequence != 1 && sequence >= i) {
		        k++;
		        if ((sequence % 2) == 0) {
		            sequence = sequence / 2;
		        } else {
		            sequence = sequence * 3 + 1;
		        }
		    }
		    //Store result in cache
		    cache[i] = k + cache[(int) sequence];
		 
		    //Check if sequence is the best solution
		    if (cache[i] > sequenceLength) {
		        sequenceLength = cache[i];
		        startingNumber = i;
		    }
		}
		return Long.toString(startingNumber);
	}

	public static void main(String[] args) {
	//	System.out.println(new P14().run());
		System.out.println(new P14().run2());
	}

}
