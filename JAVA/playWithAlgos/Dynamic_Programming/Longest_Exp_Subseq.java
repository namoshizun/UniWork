package homework;
import java.util.*;

public class longest_exp_subseq {

	/**
	 * Let A be an array with n positive numbers. An exponentially increasing
	 * subsequence is defined by a set of indices i1 < i2 <...< ik such that
	 * 2A[i1] < A[i2] and 2A[i2] < A[i3] ... and 2A[i(k+1)] < A[ik].
	 * 
	 * Your task is to design an O(n2) time algorithm for finding a longest exponentially
	 * increasing subsequence based on dynamic programming
	 */
	
	public void showLEXPS(int [] A){
		// M[i] be the length of LEXPS that ends with i. 
		int [] M = new int [A.length];
		
		
		for(int i = 1; i < A.length; ++i){
			int max = 0;
			for(int j = i - 1; j > 0; --j){
				if(2*A[j] < A[i]){
					if(M[j] > max)
						max = M[j];
				}
			}
			M[i] = max + 1;
		}
		
	// Show M, the final result will be max(M[i])
		for(int i = 1; i < M.length; ++i){
			System.out.println("M[" + i + "]: " + M[i]);
		}
	}
	
	public static void main(String[] args) {
		longest_exp_subseq run = new longest_exp_subseq();
		//             1  2  3  4  5  6  7  8
	//	int [] A = {0, 4, 1, 2, 3, 9, 7, 5, 15};
	//	int [] A = {0, 8};
		int [] A = {0, 9, 5, 7, 13,6, 100, 1};
		run.showLEXPS(A);
	}

}
