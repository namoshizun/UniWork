package LeetCode;

import java.util.*;

public class H_index {

	/*
	 * Given an array of citations (each citation is a non-negative integer) of
	 * a researcher, write a function to compute the researcher's h-index.
	 * 
	 * According to the definition of h-index on Wikipedia:
	 * "A scientist has index h if h of his/her N papers have at least h citations each, 
	 * and the other N − h papers have no more than h citations each."
	 * 
	 * For example, given citations = [3, 0, 6, 1, 5], which means the
	 * researcher has 5 papers in total and each of them had received 
	 * 3, 0, 6, 1, 5 citations respectively. Since the researcher has 3 papers with at
	 * least 3 citations each and the remaining two with no more than 3
	 * citations each, his h-index is 3.
	 * 
	 * Note: If there are several possible values for h, the maximum one is
	 * taken as the h-index.
	 * 
	 */

	public int findReferential(Integer[] arr, int start, int finish){
		// Find the number that self-describes the number i such that it is the ith biggest number
		int pivot = start + (finish - start) / 2;
		
		if(arr.length == 0)
			return 0;
		
		if(arr[pivot] == pivot + 1)
			return pivot + 1;
		
		if(finish - start < 1){
			return -1; // here is the problem
		} else {
			if (arr[pivot] > pivot) {
				// search range: arr[pivot~length-1]
				return findReferential(arr, pivot + 1, finish);
			} else {   
				// search range: arr[0~pivot]
				return findReferential(arr, start, pivot - 1);
			}
		}
	}
	
	/***
	 * Quicker to run: 
	 * inverse-sort the citation array -> rA
	 * H-index is the self-referential number of rA, say h
	 * Because h is exactly the number of elements that are equal to or greater than h
	 * And for every i > h, rA[i] must be less or equal to h.
	 * O(logn) + O(nlongn)
	 */
	public int hIndex_QuickerBS(int[] citations) {
		Integer[] citation = new Integer[citations.length];
		for(int i = 0; i < citations.length; ++i)
			citation[i] = citations[i];
		
		
		Arrays.sort(citation, Collections.reverseOrder());
		System.out.println(Arrays.asList(citation));
		return findReferential(citation, 0, citation.length - 1);
	}
	
	/**--- WAITS FOR DEBUGGING
	 * Easier to implement:
	 * Just count the number of citations whose [i] >= i
	 * O(n) + O(nlogn)
	 */
	public int hIndex_Simpler(int[] citations) {
		Arrays.sort(citations);
		int hIndex = 0;
		for(int i = 0; i < citations.length; ++i){
			if(i < citations[i])
				++hIndex;
			else
				break;
		}
		
		return hIndex;
	}

	public static void main(String[] args) {
		H_index play = new H_index();
		int[] citations = {1,2};
		System.out.println(play.hIndex_QuickerBS(citations));
		System.out.println(play.hIndex_Simpler(citations));
	}
}
