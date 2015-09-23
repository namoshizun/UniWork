package others;

import java.util.*;

public class bruteForce {

	static String input;
	static int length;
	static boolean isFound;
	static StringBuilder result;

	public static void crack(int length, StringBuilder result) {
		
		if (length == 0) {
			return;
		}

		for (int i = 0; i <= 9; ++i) {
			result.setCharAt(length - 1, Character.forDigit(i, 10));
			
			if (result.toString().equals(input)) {
				System.out.println("Cracked! your password is " + result);
				isFound = true;
				return;
			} else {
				crack(length - 1, result);
			}
		}
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a password (digits only)");
		input = sc.nextLine();
		length = 1;
		isFound = false;

		while (!isFound) {
			result = new StringBuilder("");
			for(int i = 0; i < length; ++i){
				result.append(0);
			}
			
			crack(length, result);
			++length; // increase the length of test numbers until find out the
						// password..
		}

		sc.close();
	}

}
