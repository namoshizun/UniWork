package others;

import java.util.*;

public class Pascal_Triangle {
	
	public static void drawPascalsTriangle(int n) {

		int nCk = 1;
		for (int i = 0; i <= n; i++) {
			System.out.print(nCk + " ");
			nCk = nCk * (n - i) / (i + 1);
		}
		System.out.println();

	}
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print(" please decide the number of levels for Pascal's Triangle: ");
		int levels = in.nextInt();
		int temp = levels;
		
		for (int i = 0; i < levels; ++i) {
			for(int j = 0; j <= temp; ++j){
				System.out.print(" ");
			}
			--temp;
			drawPascalsTriangle(i);
		}
	}
}
