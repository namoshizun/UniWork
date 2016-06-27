package EulerP;

import java.math.BigInteger;

public class P5 {

	/**
	 * Smallest multiple
	 * 
	 * Problem 5
	 * 
	 * 2520 is the smallest number that can be divided by each of the numbers
	 * from 1 to 10 without any remainder. What is the smallest positive number
	 * that is evenly divisible by all of the numbers from 1 to 20?
	 */
	
	static int counter = 0;
	public static void main(String[] args) {
		
		for(int i = 2520; i < Integer.MAX_VALUE; ++i){
			if(P5.isGoodNumber(i) && counter == 1){
				System.out.println(i);
				break;
			}
		}
	}
	
	public static boolean isGoodNumber(int n ){
		for(int i = 1; i <= 20; ++i){
			if(n % i != 0){
				return false;
			}
		}
		++ counter;
		return true;
	}
	/*
	public static void main(String[] args) {
		System.out.println(new P5().run());
	}
	
	
	public String run() {
		BigInteger allLcm = BigInteger.ONE;
		for (int i = 1; i <= 20; i++)
			allLcm = lcm(BigInteger.valueOf(i), allLcm);
		return allLcm.toString();
	}
	
	
	private static BigInteger lcm(BigInteger x, BigInteger y) {
		return x.divide(x.gcd(y)).multiply(y);
	}
  
  */
}
