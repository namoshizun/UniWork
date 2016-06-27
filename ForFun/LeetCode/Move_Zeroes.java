package LeetCode;

import java.util.*;

/**
 * - https://leetcode.com/problems/move-zeroes/ 
 * 
 * Given an array nums, write a function to move all 0's to the end of it while 
 * maintaining the relative order of the non-zero elements.
 * 
 * For example, given nums = [0, 1, 0, 3, 12], after calling your function, nums
 * should be [1, 3, 12, 0, 0].
 * 
 * Note: You must do this in-place without making a copy of the array. 
 * 		 Minimize the total number of operations.
 */

public class Move_Zeroes {
	
	// Simply trying to move all non-zero integers to the front.
	public void moveZeroes(int[] nums) {
		int countNonZero = 0;
		
		for(int i = 0; i < nums.length; ++i) {
			if (nums[i] != 0) {
				nums[countNonZero] = nums[i];
				++countNonZero;
			}
		}
		
		for(int i = 0; i < nums.length - countNonZero; ++i) {
			nums[countNonZero + i] = 0;
		}
	}

	public static void main(String[] args) {
		Move_Zeroes run = new Move_Zeroes();
		//int [] nums = {0, 1, 0, 3, 12};
		//int [] nums = {0, 1};
		int [] nums = {1};
		run.moveZeroes(nums);
		
		for(int i = 0; i < nums.length; ++i)
			System.out.println(nums[i]);
	}

}
