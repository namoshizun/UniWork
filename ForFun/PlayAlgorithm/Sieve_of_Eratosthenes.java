package PrimeS;

import java.util.*;

public class Sieve_of_Eratosthenes {

	public static void main(String[] args) {

		// Get the ceiling on our prime numbers
		long n;
		Scanner sc = new Scanner(System.in);
		System.out.print("enter the prime number ceiling: ");
		n = sc.nextLong();
		sc.close();
		
		List<Boolean> isprime = new ArrayList<Boolean>();
		for(int i = 0; i < n; ++i){
			isprime.add(true);
		}
		
		for(int i = 2; i < n; ++i){
			if(isprime.get(i - 1)){
				System.out.println(i);
				if(i % 20 == 0){
					System.out.println();
				}
				for(int j = i*i; j <= n; j+=i){
					isprime.set(j - 1, false); // rip out all the multiplies of i
				}
			}
		}
		

		

		
//   ==============   Using arrays for <= 20000
		
		int N = 0; // Set 'N' is you want to use this method.
		
		// Initialise the numbers array, where true denotes primes
		boolean[] isPrime = new boolean[N];
		for (int i = 1; i < N; ++i) {
			isPrime[i] = true;
		}

		// Check every number >= 2 for primality
		for (int i = 2; i <= N; ++i) {

			// i is prime if hasn't been crossed off yet.
			if (isPrime[i - 1]) {
				System.out.println(i);
				// Begin crossing off the multiplies of the number i
				//for (int j = 2 * i; j <= N; j += i) {
				  for(int j = i*i; j <= N; j +=i){
					isPrime[j - 1] = false;
				}
			}
		}
	}
}
