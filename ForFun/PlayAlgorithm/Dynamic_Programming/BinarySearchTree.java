package homework;

import java.util.*;
import java.math.*;

public class BinarySearchTree {

	/**
	 * A binary search tree is balanced if for every node in the graph, the
	 * heights of the left and right subtrees differ by at most 1. 
	 * 
	 * Let T(n) be the number of different balanced binary search trees on n keys
	 * Calculate T(n)
	 * */
	public int getBbstNum(int nNodes) {

		// M[i,k] = # of BBST with i nodes and height k.
		// More generally the size of M is nNodes + 1, but nNodes + ceiling avoids overflow when n < 10.
		int ceiling = (int) Math.ceil(2 * (Math.log(nNodes + 1) / Math.log(2)) + 2);
		int[][] M = new int[nNodes + ceiling][nNodes + ceiling];
		int numOfBBST = 0;

		// Base case: i = 0 (no keys);
		for (int i = 0; i < ceiling; ++i) {
			if(i == 0)
				M[0][i] = 1;
			else
				M[0][i] = 0;
		}
		
		// Base case h = 0 or h = 1 (empty tree or single node)
		for(int i = 1; i < nNodes + 1; ++i){
			M[i][0] = 0;
			if(i == 1)
				M[i][1] = 1;
			else
				M[i][1] = 0;
		}
		
		// Recursive case
		for(int i = 1; i < nNodes + 1; ++i){
			for(int height = 0; height < ceiling; ++height){
				if(height > 1){
					int sum = 0;
					for(int root = 1; root < i + 1; ++root){
						// condition on the root and heights of left and right children
						sum += M[root - 1][height - 1]* M[i - root][height - 1] +
								M[root - 1][height - 2]* M[i - root][height - 1] +
								M[root - 1][height - 1]* M[i - root][height - 2];
					}
					M[i][height] = sum;
				}
			}
		}
		
		for(int height = 0; height < ceiling; ++height){
			numOfBBST += M[nNodes][height];
		}
		
		return numOfBBST;
	}

	public static void main(String[] args) {
		BinarySearchTree run = new BinarySearchTree();
		int nNodes = new Scanner(System.in).nextInt();
		System.out.println(run.getBbstNum(nNodes));
	}
}
