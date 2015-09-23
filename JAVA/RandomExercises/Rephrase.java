package PlayWithWords;

import java.util.*;

/*
 * Move the first word to the last
 * 
 * example:
 * > Enter a line of text. No punctuation please.
 * Java is the language
 * > I have rephrased that line to read:
 * Is the language Java
 */

public class rephrase {

	public static void main(String[] args) {
		System.out.println("Enter a line of text: (no puncuation please..)");
		Scanner keyboard = new Scanner(System.in);
		String input = keyboard.nextLine();
		String output = new String();

		Scanner sc = new Scanner(input);

		if (!input.isEmpty()) {
			String first = sc.next();
			String second = new String();

			if (sc.hasNext()) {
				// concatenate second string with first char converted to be
				// upper case
				second = sc.next();
				output += Character.toUpperCase(second.charAt(0)) + second.substring(1);
				// concatenate other Strings
				output += input.substring(input.indexOf(second) + second.length());
				output += " " + first;

			} else {
				output = first;
			}
		}
		System.out.println("I have rephrased tha line to read\n" + output);
	}
}
