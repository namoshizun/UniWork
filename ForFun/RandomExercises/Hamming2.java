package Melt_Your_Brain;

public class Hamming2 {

	/**
	 * @return True if and only if every character in the input String s is one
	 *         of a, A, c, C, g, G, t or T. False if s is null or empty.
	 */
	public static boolean isDNASequence(String s) {
		boolean isValid = true;
		if (s.length() == 0 || s.equals(null))
			return false;
		String caselessS = s.toUpperCase();
		for (int i = 0; i < s.length(); i++) {
			switch (caselessS.charAt(i)) {
				case 'A':
				case 'C':
				case 'G':
				case 'T':
					break;
				default:
					isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * Get the distance matrix
	 * 
	 * Matrix is a simple, symmetrical square which compares all strings to all other strings.
	 * Each string will be compared twice to every other string (both ways round), and once to itself.
	 * Method first checks each string to make sure it is valid.
	 *
	 * Example distance matrix:
	 * 0 3 5 1
	 * 3 0 4 2
	 * 5 4 0 8
	 * 1 2 8 0
	 * 
	 * @param sequences
	 *            - array of sequences
	 * @return distance matrix
	 */
	public static int[][] getDistances(String[] sequences) {
		int goodStringCount = 0;
		for (String s: sequences) {
			if (isDNASequence(s))
				goodStringCount++;
		}
		//Counts number of valid DNA sequences

		if (goodStringCount == 0)
			return null;
		// handles no or bad input
		
		int goodStringIndex = 0;
		String[] goodString = new String[goodStringCount];
		for (String s : sequences) {
			if (isDNASequence(s)) {
				goodString[goodStringIndex] = s.toUpperCase();
				goodStringIndex++;
			}
		}

		/* Puts all valid DNA sequences into a new array.
		 * Above commands could have been executed in the main method
		 * but PASTA seems to ignore the main method when testing
		 */

		int matrixSize = goodString.length;
		int[][] distanceMatrix = new int[matrixSize][matrixSize];
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				distanceMatrix[i][j] = getHammingDistance(goodString[i], goodString[j]);
			}
		}
		return distanceMatrix;
	}

	/**
	 * Get the hamming distance between two string sequences
	 * 
	 * @param sequence1
	 *            - the first sequence
	 * @param sequence2
	 *            - the second sequence
	 * @return the hamming distance
	 */
	public static int getHammingDistance(String sequence1, String sequence2) {
		if (sequence1.length() != sequence2.length())
			return -1; //identifies an invalid calculation
		int hammingDistance = 0;
		for (int i = 0; i < sequence1.length(); i++) {
			if (sequence1.charAt(i) != sequence2.charAt(i))
				hammingDistance++;
		}
		return hammingDistance;
	}

	/**
	 * Main method
	 * 
	 * 1. Go through the parameters 2. Ensure they are valid DNA sequences 3.
	 * Get the hamming distance matrix 4. Print out the highest difference
	 * 
	 * @param args
	 *            - program arguments
	 */
	public static void main(String[] args) {

		int[][] distanceMatrix = getDistances(args);
		if (distanceMatrix == null)
			return;
		
		int largestDistance = -1;
		for (int i = 0; i < distanceMatrix.length; i++) {
			for (int j = 0; j < distanceMatrix.length; j++) {
				if (distanceMatrix[i][j] > largestDistance && i != j)
					// ignores Hamming distances of a string to itself (i.e the top left to bottom right diagonal of the matrix)
					largestDistance = distanceMatrix[i][j];
			}
		}
		//Finds the largest Hamming distance in the Matrix
		
		if (largestDistance >= 0)  // prints nothing if all strings are invalid
			System.out.println(largestDistance);
	}
}