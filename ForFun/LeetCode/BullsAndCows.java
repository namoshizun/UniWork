package LeetCode;

import java.math.*;
import java.util.*;

/**
 * Leetcode question 299: Bulls and Cows.
 * 
 * Question: You are playing the following Bulls and Cows game with your friend:
 * You write down a number and ask your friend to guess what the number is. Each
 * time your friend makes a guess, you provide a hint that indicates how many
 * digits in said guess match your secret number exactly in both digit and
 * position (called "bulls") and how many digits match the secret number but
 * locate in the wrong position (called "cows"). Your friend will use successive
 * guesses and hints to eventually derive the secret number.
 * 
 * About the game Bulls and Cows: https://leetcode.com/problems/bulls-and-cows/
 */

// Lesson learned: Dont simply parse char to int by '-48', otherwise problem
// arises for '0'
public class BullsAndCows {

	private class Player {
		String name;
		int numWins;

		public Player(String name) {
			this.name = name;
			this.numWins = 0;
		}

		private String getName() {
			return this.name;
		}

		private void winGame() {
			++this.numWins;
		}
	}

	public String getGuess(String hint) {
		return null;
	}

	// Brute Force
	public String getHint(String secret, String guess) {
		String hint = new String();
		int length = secret.length();
		int[] secretDigits = new int[length];
		int[] guessDigits = new int[length];
		int numBulls = 0, numCows = 0;
		int digit_g, digit_s;

		for (int i = 0; i < length; ++i) {
			secretDigits[i] = Character.getNumericValue(secret.charAt(i));
			guessDigits[i] = Character.getNumericValue(guess.charAt(i));
			// Check for Bulls as storing digits into an array
			if (secretDigits[i] == guessDigits[i]) {
				numBulls++;
				secretDigits[i] = -1;
				guessDigits[i] = -2;
			}
		}

		// Check for Cows
		for (int i = 0; i < length; ++i) {
			for (int j = 0; j < length; ++j) {
				if (guessDigits[i] == secretDigits[j]) {
					numCows++;
					secretDigits[j] = -1;
					guessDigits[i] = -2;
					break;
				}
			}
		}

		hint = numBulls + "A" + numCows + "B";
		return hint;
	}

	// Higher Performance
	public String getHint_smart(String secret, String guess) {

		if (secret == null || guess == null || secret.length() != guess.length()) {
			return "";
		}

		int countA = 0;
		int countB = 0;
		// count the number of occurrence of digit 0-9. 
		int[] count = new int[10];

		for (int i = 0; i < secret.length(); i++) {
			if (secret.charAt(i) == guess.charAt(i)) {
				// Count Bull
				countA++;
			} else {
				// for each digit the 'secret' met, inc.
				count[secret.charAt(i) - '0']++;
				if (count[secret.charAt(i) - '0'] <= 0) {
					countB++;
				}
				
				// for each digit the 'guess' met, dec. 
				count[guess.charAt(i) - '0']--;
				if (count[guess.charAt(i) - '0'] >= 0) {
					countB++;
				}
			}
		}

		return String.valueOf(countA) + "A" + String.valueOf(countB) + "B";
	}

	public String play(Player secretHolder, Player secretGuesser) {
		String winner = null;

		return winner;
	}

	public static void main(String[] args) {

		BullsAndCows game = new BullsAndCows();
		// TEST
		// INPUT (secretNum. guessNum)
		System.out.println(game.getHint("11", "10"));
		System.out.println(game.getHint("011", "110"));

		Player p1 = game.new Player("Smart");
		Player p2 = game.new Player("Pretty");

		// p1 be the holder and p2 be the guesser, play 10 times
		/*
		 * for (int i = 0; i < 10; ++i) { String winner = game.play(p1, p2); if
		 * (winner.equals("Smart")) { p1.numWins++; } else if
		 * (winner.equals("Pretty")) { p2.numWins++; } }
		 * 
		 * // p2 be the holder and p1 be the guesser, play 10 times for (int i =
		 * 0; i < 10; ++i) { String winner = game.play(p2, p1); if
		 * (winner.equals("Smart")) { p1.numWins++; } else if
		 * (winner.equals("Pretty")) { p2.numWins++; } }
		 * 
		 * System.out.println("The winner is: " + (p1.numWins > p2.numWins ?
		 * p1.name : p2.name));
		 */
	}
}
