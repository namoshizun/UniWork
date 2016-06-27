package Melt_Your_Brain;

import java.util.*;

public class LogicalPrac {

	public static void main(String[] args) {
		Date begin = new Date();
		/*
		 * Task 1; Assuming you have small bars(2 kilo each) and big bars(5
		 * kilos each) enter you goal bar in kilos and program tells you the
		 * option that takes least bars.
		 */
		Scanner input = new Scanner(System.in);
		try {

			System.out.println("How many kilos of choclates do you want?");
			// int goal = input.nextInt();
			int goal = 2;
			System.out.println("Small bars: " + makeChoclate(goal)[0]);
			System.out.println("Big bars: " + makeChoclate(goal)[1]);

		} catch (NullPointerException e) {
			System.out.println("Cannot be soloved~ ");
			System.out.println(9 % 5);
		}

		/*
		 * Task 2:Find all the circular primes under number input. eg. There are
		 * thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71,
		 * 73, 79, and 97
		 */
		System.out.println("Enter a number and this gives me headache: ");
	//	int num = input.nextInt();
		int num = 100000;
		input.close();
		if (getCircularPrimes(num) == null) {
			System.out.println("No circular primes found in this range");
		} else {
			int[] primes = getCircularPrimes(num);
			System.out.println("Ready~~~ GO!");
			for (int i = 0; i < primes.length; ++i) {
				System.out.print(primes[i] + " ");
				if (i % 10 == 0) {
					System.out.println();
				}
			}
		}
		System.out.println();
		System.out.println("Time taken is "
				+ (new Date().getTime() - begin.getTime()) + " ms");
	}

	// This is for the task 1
	public static int[] makeChoclate(int goal) {
		int small = 2;
		int big = 5;
		int[] allocation = new int[2];
		// 0 position is for small bars.
		if ((((goal % big) % small) < small) && ((goal % big) % small) != 0) {
			return null;
		} else {
			allocation[0] = (goal % big) / small;
			allocation[1] = (int) (goal / big);
			return allocation;
		}
	}

	/*
	 * The number, 197, is called a circular prime because all rotations of the
	 * digits: 197, 971, and 719, are themselves prime.
	 * 
	 * There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37,
	 * 71, 73, 79, and 97.
	 */
	public static int[] getCircularPrimes(int num) {
		List<Integer> primesArrList = new ArrayList<Integer>();
		for (int i = 2; i < num; ++i) {
			if (isCircularPrime(i)) {
				primesArrList.add(i);
			}
		}

		int primes[] = new int[primesArrList.size()];
		for (int i = 0; i < primes.length; ++i) {
			primes[i] = primesArrList.get(i).intValue();
		}
		return primes;
	}

	public static boolean isCircularPrime(int num) {
		// First of all, the number itself has to be a prime number
		if(!isPrime(num)){
			return false;
		}
		
		String strForm = Integer.toString(num); // num = 197, length = 3;
		String[] segments = new String[strForm.length()];
		String[] rotationStr = new String[strForm.length()];

		for (int i = 0; i < strForm.length(); ++i) {
			
			if (i == 0) {
				// Then, split the number into its segments and stored into
				// a String Array.
				for (int j = 0; j < strForm.length(); ++j) {
					segments[j] = Character.toString(strForm.charAt(j));
				}
			}
			for (int k = 0; k < strForm.length(); ++k) {
				int index;
				if (i + k > strForm.length() - 1) {
					index = 0;
				} else {
					index = i + k;
				}
				rotationStr[i] += segments[index];
			}
			rotationStr[i] = rotationStr[i].replaceAll("null", "");
		}
		boolean isCircularPrime = true;
		for (int i = 0; i < strForm.length(); ++i) {
			if (!isPrime(Integer.parseInt(rotationStr[i]))) {
				isCircularPrime = false;
			}
		}
		return isCircularPrime;

	}

	public static boolean isPrime(int num) {
		
		boolean prime = true;
		if (num == 2) {
			prime = true;
		} else if (num % 2 == 0) {
			prime = false;
		} else {
			for (int j = 3; j <= Math.sqrt(num); j++) {
				if (num % j == 0) {
					prime = false;
				}
			}
		}
		return prime;
	}
}
