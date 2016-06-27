package others;

import java.util.*;

/**
 * We can think of a cyclic word as a word written in a circle. To represent a
 * cyclic word, we choose an arbitrary starting position and read the characters
 * in clockwise order. So, "picture" and "turepic" are representations for the
 * same cyclic word.
 * 
 * You are given a String[] words, each element of which is a representation of
 * a cyclic word. Return the number of different cyclic words that are
 * represented.
 * 
 * eg. {the, brown, fox, oxf} will return 3
 */
public class CountCyclicWord {

	public int countCyclicWords(String[] input) {
		int count = 0;
		int inputSize = input.length;
		List<String> toTest = new LinkedList<String>();

		if (input.length <= 1) {
			return 0;
		} else {
			for (int i = 0; i < inputSize; ++i) {
				toTest.add(input[i]);
			}
		}

		while (!toTest.isEmpty()) {
			StringBuilder strBuilder = new StringBuilder();
			String testStr;
			String baseStr = toTest.remove(0);
			++count;

			int len = baseStr.length();
			strBuilder.append(baseStr);
			strBuilder.append(baseStr.substring(0, len - 2));
			testStr = strBuilder.toString();
			
			for(int i = 0; i < inputSize - count; ++i) {
				if(testStr.contains(toTest.get(i))) {
					toTest.remove(i);
				}
			}

		}

		return count;
	}

	public static void main(String[] args) {
		String[] input = new String[] { "the", "brown", "fox", "oxf" };
		CountCyclicWord c = new CountCyclicWord();
		System.out.println(c.countCyclicWords(input));
	}

}
