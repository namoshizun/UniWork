package EulerP;

import java.util.*;

/**
 * If the numbers 1 to 5 are written out in words: one, two, three, four, five,
 * then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.
 * 
 * If all the numbers from 1 to 1000 (one thousand) inclusive were written out
 * in words, how many letters would be used?
 * 
 * 
 * NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and
 * forty-two) contains 23 letters and 115 (one hundred and fifteen) contains 20
 * letters. The use of "and" when writing out numbers is in compliance with
 * British usage.
 */

public class P17 {

	public static void main(String[] args) {

		int countChar = 0;

		for (int i = 12; i < 13; ++i) {

			int digit = 1;
			int temp = 1;

			for (int j = 1; j <= Integer.toString(i).length(); ++j) {

				if (i >= 10 && i < 20) {
					countChar += betweenTenAndTwenty(i);
					break;
				} else {
					if (j == 1) {
						digit = i % 10;
						countChar += getChars(digit, j);
					} else if (j >= 2) {
						temp *= 10;
						digit = (i / temp) % 10;
						countChar += getChars(digit, j);
					}
				}
			}
		}
		System.out.println(countChar); // 11 is "one thousand"
	}

	public static int getChars(int num, int place) {

		if (place == 1) {
			return underTen(num);
		} else if (place == 2) {
			return aboveTwenty(num);
		} else if (place == 3) {
			return underTen(num) + 10; // 10 is the number of letters in
										// "hundred and"
		}
		return 0;
	}

	public static int underTen(int num) {
		switch (num) {
		case 1:
		case 2:
		case 6:
			return 3;
		case 4:
		case 5:
		case 9:
			return 4;
		case 3:
		case 7:
		case 8:
			return 5;

		default:
			return 0;
		}
	}

	public static int betweenTenAndTwenty(int num) {
		switch (num) {
		case 10:
			return 3;
		case 11:
		case 12:
			return 6;
		case 15:
		case 16:
			return 7;
		case 13:
		case 14:
		case 19:
		case 18:
			return 8;
		case 17:
			return 9;

		default:
			return 0;
		}
	}

	public static int aboveTwenty(int num) {
		switch (num) {
		case 2:
		case 3:
		case 8:
			return 6;
		case 4:
		case 5:
		case 6:
		case 9:
			return 5;
		case 7:
			return 7;

		default:
			return 0;
		}
	}
}
