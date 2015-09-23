package homework;

import java.math.*;
import java.util.*;

/**
 * - 
 * Description: A binary search tree is balanced if for every node in the graph, the
 * heights of the left and right subtrees differ by at most 1.
 * 
 * Let T(n) be the number of different balanced binary search trees on n keys.
 * Implement an algorithm that uses dynamic programming to compute T(n) for a
 * given input n.
 * 
 * Your algorithm should run in O(n^2 logn) time. Your program should take a
 * single command line argument (the number of keys) and print the number of
 * balanced binary trees.
 * 
 * Recurrence: T(n) =Sum{T(k-1)T(n-k)}, k = [lowerBound, upperBound] is the kth
 * key By finding the minimum size of the lft subtree, we can then determine the
 * range of 'k'.
 */

public class bbst {

	/*-
	 * Constructor:
	 * levelCapacity shows the max total number of nodes with i many levels. 
	 * The first level is level 1
	 */
	List<Integer> levelCapacity;
	BigInteger[] results;

	public bbst(int bufferSize) {
		levelCapacity = new ArrayList<Integer>();
		results = new BigInteger[bufferSize]; // WORST DESIGN EVER :D
		for (int i = 0; i < bufferSize; ++i)  results[i] = BigInteger.valueOf(-1);
		for (int i = 0; i <= 30; ++i)		  levelCapacity.add((int) (Math.pow(2, i) - 1));
	}

	public int getLeftSubtreeSize(int nNodes) {
		int nLeft = 0;
		int nRight = 0;

		for (int i = 1; i < levelCapacity.size(); ++i) {
			nLeft = levelCapacity.get(i);
			nRight = levelCapacity.get(i + 1);
			int tmp = nNodes - 1;

			if (nLeft + nRight >= tmp) {
				for (int j = nRight; j >= nLeft; --j) {
					for (int k = nLeft; k > levelCapacity.get(i - 1); --k) {
						if (j + k == tmp)
							return k;
					}
				}
			}
		}
		return 0; // this should never happen~
	}

	public boolean isFullBinary(int size){
		for(Integer i : levelCapacity){
			if(size == i)
				return true;
		}
		return false;
	}
	
	public BigInteger bbstNumbers(int nNodes) {
		BigInteger result = BigInteger.valueOf(0);

		// Base cases
		if (nNodes == 1) return BigInteger.ONE;
		if (nNodes == 2) return BigInteger.valueOf(2);
		if (!results[nNodes].equals(BigInteger.valueOf(-1))) {
			return results[nNodes];
		}
		// Otherwise
		int leftSize = getLeftSubtreeSize(nNodes);
		int lower = leftSize + 1;
		int upper = nNodes - lower + 1;
			
		for (int k = lower; k <= upper; ++k) {
			int rightSize = nNodes - k;
			if ((k - 1) > 2*rightSize && isFullBinary(k - 1)){
				result = result.add(bbstNumbers(rightSize));
			} else if (2*(k - 1) < rightSize && isFullBinary(rightSize)){
				result = result.add(bbstNumbers(k - 1));
			} else {
				result = result.add(bbstNumbers(k - 1).multiply(bbstNumbers(nNodes - k)));
			}
		}

		if (results[nNodes].equals(BigInteger.valueOf(-1)))
			results[nNodes] = result;
		return result;
	}

	public static void main(String[] args) {
		int bufferSize = 200;
		bbst run = new bbst(bufferSize);
		System.out.println(run.bbstNumbers(15));
		System.out.println(run.bbstNumbers(17));
		//System.out.println(run.bbstNumbers(Integer.parseInt(args[0])));
	}
}
