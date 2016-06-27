package EulerP;

public class P2 {
	
	/*-
	 * If we look at the Fibonacci sequence: 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89
	 * 
	 * We may notice the pattern that every third number is even starting at F3,
	 * so if we can express Fn in terms of Fn-3, Fn-6 then we only has to deal
	 * with even numbers
	 * 
	 * We start out with
	 * 
	 * Fn = Fn-1 + Fn-2 = Fn-2 + Fn-3 + Fn-3 +Fn-4 = (since Fn-1 = Fn-2 + Fn-3
	 * and so on) Fn-3 + Fn-4 + Fn-3 +Fn-3 +Fn-4 = 3Fn-3 + 2Fn-4 = 3Fn-3 + Fn-4
	 * + Fn-5 + Fn-6) 
	 * = 4Fn-3 + Fn-6 (since Fn-4 + Fn-5 = Fn-3)
	 */
	public static String evensOnly() {

		long fib3 = 2;
		long fib6 = 0;
		long result = 2;
		long summed = 0;

		while (result < 2000000000) {
			summed += result;

			result = 4 * fib3 + fib6;
			fib6 = fib3;
			fib3 = result;
		}
		return Long.toString(summed);
	}
	
	public static String reduceWriteOperation(){
		long[] fib = {2,0};
		int i = 0;
		long summed = 0;
		 
		/** Jumping from 0 and 1  */
		while (fib[i] < 4000000) {
		    summed += fib[i];
		    i = (i + 1) % 2;
		    fib[i] = 4 * fib[(i + 1) % 2] + fib[i];
		}
		
		return Long.toString(summed);
	}
	
	public static String bruteForce() {

		long fib1 = 1;
		long fib2 = 1;
		long result = 0;
		long sumOfEvens = 0;

		while (result < 2000000000) {
			if (result % 2 == 0) {
				sumOfEvens += result;
			}
			result = fib1 + fib2;
			fib1 = fib2;
			fib2 = result;
		}

		return Long.toString(sumOfEvens);
	}

	public static void main(String[] args) {

		System.out.println(new P2().bruteForce());
		System.out.println(new P2().evensOnly());
		System.out.println(new P2().reduceWriteOperation());
	}
}