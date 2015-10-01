import java.util.*;

public class bf_crackpw {

	static String input;
	static int length;
	static StringBuilder result;

	public static void crack(int length, StringBuilder result) {
		
		if (length == 0) {
			return;
		}

		for (int i = 0; i <= 9; ++i) {
			result.setCharAt(length - 1, Character.forDigit(i, 10));
			if (result.toString().equals(input)) {
				// Very brutal way to force to stop a recursion..
				throw new NullPointerException("Cracked! your password is " + result);
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

		try{
			while (true) {
				result = new StringBuilder("");
				for(int i = 0; i < length; ++i){
					result.append(0);
				}
			
				crack(length, result);
				++length; // increase the length of test numbers until find out the
						// password..		
			}		
		} catch (NullPointerException e){
			System.out.println(e.getMessage());
		}
		sc.close();
	}
}
