package others;

import java.util.*;

public class validPassword {

	public int solution_1(String S) {
		// Base case : single character password
		int inputLen = S.length();
		if (inputLen == 1) {
			if (Character.isUpperCase(S.charAt(0)))
				return 1;
			else
				return -1;
		}

		boolean isValid = false;
		int max = 0;
		for (int i = 0; i < inputLen; ++i) {
			int substrLen = 0;
			// Test on substring sub {i, ..., j}
			for (int j = i; j < inputLen; ++j) {
				char c = S.charAt(j);

				if (Character.isUpperCase(c)) {
					// Sub is valid if it contains an upper letter
					isValid = true;
					// Sub is invalid if it contains a digit.
				} else if (Character.isDigit(c)) {
					/*
					 * Sub is invalid if it contains a digit So stop here and
					 * the substring {i, ..., j} is the longest valid password
					 * that starts at i and end at j.
					 */
					break;
				}
				++substrLen;
			}

			// update the maximum
			if (isValid) {
				if (substrLen > max)
					max = substrLen;
			}
		}

		if (isValid) return max;
		else         return -1; // when there is no valid password in S
	}

	public int solution_2(int[] A_weight, int[] B_toFloor, int M_topLevel, int X_capacity, int Y_maxWeight) {
		/*
		 * Base case: if the people at the front blocks the entire queue forever
		 * then the elevator doesn't have to move to send anybody.
		 */
		if (A_weight[0] > Y_maxWeight)
			return 0;

		int numTotalStops = 0;
		int N = A_weight.length;
		
		// Initialize a queue that contains the (weight, targetFloor) of each person
		Queue<int[]> queue = new LinkedList<int[]>();
		for (int i = 0; i < N; ++i) {
			queue.add(new int[] { A_weight[i], B_toFloor[i] });
		}

		while (!queue.isEmpty()) {
			int totalWeight = Y_maxWeight;
			int numStops = 0;
			int numPassenger = 0;
			// Use Set data-structure so that duplicates are removed
			Set<Integer> stops = new HashSet<Integer>();
			
			/*
			 *  Get as many people on elevator as possible for once
			 *  Get the number of stops needed for this carriage
			 */
			while (!queue.isEmpty() 
					&& totalWeight - queue.peek()[0] >= 0
					&& numPassenger + 1 <= X_capacity) {
				int[] person = queue.poll();
				totalWeight -= person[0];
				stops.add(person[1]);
				++numPassenger;
			}
			
			numStops = stops.size();
			numTotalStops += numStops + 1; // once is for moving back to ground
		}

		return numTotalStops;
	}

    public int solution_3(int[] A, int K) {
        int n = A.length;
        int best = 0;
        int count = 1;
        for (int i = 0; i < n - K- 1; i++) { 
            if (A[i] == A[i + 1])
                count = count + 1;
            else
                count = 0;
            if (count > best)
                best = count;
        }
        int result = best + K + 1;

        return result;
    }
    
	public static void main(String[] args) {
		validPassword test = new validPassword();
		//System.out.println(test.solution_1("000000aaaaaaaaaasdasdadasdasd0000aB0"));

		int M = 5;
		int X = 100;
		int Y = 1000000000;
		int[] A = new int[] { 60, 80, 40 };
		int[] B = new int[] { 2, 2 ,2 };
		//	System.out.println(test.solution_2(A, B, M, X, Y));
		
		int[] a = new int[] {1, 1, 3, 3, 3, 4, 5, 5, 5, 5}; // 5
		int[] a_1 = new int [] {1, 2, 3, 4, 5}; // 2 
		int[] a_2 = new int[] {1, 2, 3, 3, 3, 3, 3, 3, 3 , 3 ,3}; // 9
		int[] a_3 = new int[] {1, 1, 1, 1, 2, 2, 3}; // BUGGY case, output 7 but should be 6
		int[] a_4 = new int [] {1, 2, 2, 2, 2, 3, 3}; // 6
		
	//	System.out.println(test.solution_3(a, 2));
	//	System.out.println(test.solution_3(a_1, 2));
	//	System.out.println(test.solution_3(a_2, 3));
	//	System.out.println(test.solution_3(a_3, 2));
		System.out.println(test.solution_3(a_4, 2));
		
	}

}
