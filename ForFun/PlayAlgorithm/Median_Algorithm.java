package Search;

import java.util.*;

/*
 * Using the Median-of-Median algorithm to find the kth smallest number of an unsorted array.
 */

public class Median_Algorithm {

	public static void main(String[] args) {

		// For testing:
		// int[] numbers = new int[]{1,2,3,4,5,6,7,8,9};

/*		Scanner in = new Scanner(System.in);
		System.out.print("Enter a serie of numbers: ");
		String input = in.nextLine();
		String[] temp = input.split(" ");
		int[] numbers = new int[temp.length];
		for (int i = 0; i < temp.length; ++i) {
			numbers[i] = Integer.parseInt(temp[i]);
		}
		
		System.out.println(selectKth(numbers, 3));
*/
		int length = 1000;
		int[] numbers = new int[length];
		Random r = new Random();
		for(int i = 0; i < length; ++i){
			numbers[i] = r.nextInt(300) + 1;
		}
		System.out.println(numbers[4]);
		for(int i = 0; i < 400; ++i){
			System.out.println("The " + (10 + i) + "th smallest number is " + selectKth(numbers, i));
		}
		System.out.println(selectKth(numbers, 8));
	}

	public static int selectKth(int[] arr, int k) {
		// Condition Check
		if (arr == null || arr.length <= k)
			throw new Error();

		int from = 0, to = arr.length - 1;

		// if from == to we reached the kth element
		while (from < to) {
			int r = from, w = to;
			int mid = arr[(r + w) / 2];

			// stop if the reader and writer meets
			while (r < w) {

				if (arr[r] >= mid) { // put the large values at the end
					int tmp = arr[w];
					arr[w] = arr[r];
					arr[r] = tmp;
					w--;
				} else { // the value is smaller than the pivot, skip
					r++;
				}
			}

			// if we stepped up (r++) we need to step one down
			if (arr[r] > mid)
				r--;

			// the r pointer is on the end of the first k elements
			if (k <= r) {
				to = r;
			} else {
				from = r + 1;
			}
		}

		return arr[k];
	}

}
