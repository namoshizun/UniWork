package EulerP;

import java.util.*;
import java.math.*;

public class P21 {

	/**
	 * Amicable numbers Problem 21
	 * 
	 * Let d(n) be defined as the sum of proper divisors[YUE SHU] of n (numbers
	 * less than n which divide evenly into n).If d(a) = b and d(b) = a, where a
	 * != b, then a and b are an amicable pair and each of a and b are called
	 * amicable numbers.
	 * 
	 * For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22,
	 * 44, 55 and 110; therefore d(220) = 284. The proper divisors of 284 are 1,
	 * 2, 4, 71 and 142; so d(284) = 220. Evaluate the sum of all the amicable
	 * numbers under 10000.
	 */

	public int getSiever(int num) {
		int i;
		for (i = 2; i <= num / 2; ++i) {
			if (num % i == 0) {
				break;
			}
		}
		return i + 1 > num / 2 ? 0 : i;// return 0 means is prime
	}

	public int getSum(int num) {
		int sum = 0;
		for (int i = 1; i <= num / 2; ++i) {
			if (num % i == 0) {
				sum += i;
			}
		}
		return sum;
	}

	public void printNums(int [] nums){
		for(int i = 0; i < nums.length; ++i){
			System.out.println(nums[i]);
		}
	}
	
	public void getAllDivSums(int num) {
		int[] nums = new int[num + 1];
		// We start from 4, we already know that 1~4 are all not possible cases,
		// but 1~3 are hard to startup.
		int prev = 4;
		int sum = 0;
		int next = 0;
		num -= 3;

		while (num != 0) {
			int multiplier = getSiever(prev);
			--num;
			/*
			 * if startup number is prime or has already considered, jump to
			 * next one
			 */
			if (multiplier == 0 || nums[prev] != 0) {
				++prev;
				continue;
			} else {
				// Otherwise we found a good startup number, set SUM first
				sum = getSum(prev);
				nums[prev] = sum;
			}
			
			while (true) {
				if((next = prev * multiplier) > num) break;
				next = prev * multiplier;
				sum += prev;
				nums[next] = sum;
				prev = next; // Advance to the next number;
				--num;
			}
		}
		
		printNums(nums);
	}

	public static void main(String[] args) {
		P21 player = new P21();
		player.getAllDivSums(10);
	}

}
