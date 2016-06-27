package EulerP;

public class P9 {

	/**
	 * Special Pythagorean triplet Problem 9 A Pythagorean triplet is a set of
	 * three natural numbers, a < b < c, for which,
	 * 
	 * a^2 + b^2 = c^2 For example, 32 + 42 = 9 + 16 = 25 = 52.
	 * 
	 * There exists exactly one Pythagorean triplet for which a + b + c = 1000.
	 * Find the product abc.
	 */
	public static void main(String[] args) {

		System.out.println(new P9().run());
	}

	public String run() {
		for (int a = 1; a < 1000; a++) {
			for (int b = a + 1; b < 1000; b++) {
				int c = 1000 - a - b;
				if (a * a + b * b == c * c) // Note: This implies b < c
					return Integer.toString(a * b * c);
			}
		}
		throw new AssertionError("Not found");
	}

}
