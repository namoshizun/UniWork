package EulerP;
import java.math.*;
import java.util.*;

public class P20 {

	/**
	 * Factorial digit sum Problem 20 
	 * 
	 * n! means n * (n - 1) * ... * 3 * 2 * 1
	 * 
	 * For example, 10! = 10*9*...*3*2*1 = 3628800, and the sum of the
	 * digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.
	 * 
	 * Find the sum of the digits in the number 100!
	 */
	public static void main(String[] args) {
		// Brute force
		int sum = 0;
		BigInteger factorial = BigInteger.valueOf(1);
		for(int i = 100; i >= 1; --i){
			factorial = factorial.multiply(BigInteger.valueOf(i));
		}
		
		String num = factorial.toString();
		System.out.println(num);
		for(int i = 0; i < num.length(); ++i){
			sum += num.charAt(i);
		}
		
		System.out.println(sum);
	}

}
