
//https://leetcode.com/problems/implement-strstr/
import java.util.*;

public class KMP_Algorithm {

	/*
	 *  Very moderate dynamic programming algorithm. 
	 *  
	 *  Returns the partial match table of 'word', 
	 *  the table's actual index begins at 1.
	 */
	public List<Integer> getPartialMatchTable(String word) {
		final int length = word.length();

		if (length == 0)
			return Collections.emptyList();

		final List<Integer> partialTable = new ArrayList<>(length + 1);
		partialTable.add(-1);
		partialTable.add(0);

		final char firstChar = word.charAt(0);
		int prevVal;
		for (int idx = 1; idx < word.length(); idx++) {
			prevVal = partialTable.get(idx);
			if (prevVal == 0) {
				if (word.charAt(idx) == firstChar)
					/*
					 * Because all the prefixes contain the first character of
					 * word we can use it as a pattern checker, failed to match
					 * will be ignored.
					 */
					partialTable.add(1);
				else
					partialTable.add(0);
			} else if (word.charAt(idx) == word.charAt(prevVal))
				partialTable.add(prevVal + 1);
			else
				partialTable.add(0);
		}

		// Returns an unmodifiable view of the specified list. 
		// This is a good software engineering practice :)
		return Collections.unmodifiableList(partialTable);
	}

	// use KMP algorithm to implement strStr()
	public int strStr(String haystack, String needle) {
		List<Integer> partialTable = getPartialMatchTable(needle);
		int needleLen = needle.length();
		int haystackLen = haystack.length();
		int numOfMatch = 0, ndlIdx = 0, hayIdx = 0;
		// Boundary check
		if(needleLen > haystackLen) return -1;
		
		while (true) {
			/*
			 * two return cases:
			 * 1. the substring has been found, break;
			 * 2. there is no needle can be possibly found;
			 */
			if(ndlIdx >= needleLen) break;
			if(haystackLen - hayIdx < needleLen) return -1;
			
			
			if(haystack.charAt(hayIdx + ndlIdx) == needle.charAt(ndlIdx)){
				++ndlIdx;
				++numOfMatch;
			} else {
				/*
				 * In the case of unmatched character, forward pointer according to:
				 *  = numOfMatch - partial match value of matching substring.
				 *  
				 *  note: partialTable.get(0) = -1; 
				 *  so if the first char is unmatched, advance = 1;d
				 */
				int advance = numOfMatch - partialTable.get(numOfMatch);
				hayIdx += advance;
				ndlIdx = 0;
				numOfMatch = 0;
			}
		}
		
		return hayIdx;
	}

	public static void main(String[] args) {
		KMP_Algorithm test = new KMP_Algorithm();
		System.out.println(test.getPartialMatchTable("d"));
		System.out.println(test.strStr("BBC ABCDAB ABCDABCDABDE", "ABCDABD"));
	}

}
