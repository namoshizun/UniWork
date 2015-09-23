package homework;

import java.util.*;

public class domino {

	int bufferSize = 300;
	int[][] board = new int[bufferSize][bufferSize];
	int targetFlow = 1;
	List<String> BLACK = new ArrayList<String>();
	List<String> WHITE = new ArrayList<String>();

	/*
	 * The graph description should give a graph AUBU(source + sink), whose max
	 * flow is equal to the target flow;
	 */
	public void output() {

		int numBlack = BLACK.size();
		int numWhite = WHITE.size();
		targetFlow = numWhite > numBlack ? numWhite : numBlack;
		int ColorA = targetFlow == numWhite ? -1 : -2;
		int ColorB = targetFlow == numWhite ? -2 : -1;
		int sink = numBlack + numWhite + 1;
		int A = 1;
		int B = targetFlow + 1;

		// Print T, source and sink.
		System.out.println(targetFlow + "\n" + 0 + "\n" + sink);

		// Print edges
		for (int i = 1; i < bufferSize; ++i) {
			for (int j = 1; j < bufferSize; ++j) {
				if (board[i][j] == ColorA) { 
					// from source to a cell belongs to A
					System.out.println(0 + "," + A + "," + 1);
					// search in four directions of the cell. 
					for(int dx = -1; dx <= 1; ++dx){
						for(int dy = -1; dy <= 1; ++dy){
							// found an adjacent block
							int cell = board[i + dx][j + dy];
							if(dx != dy && cell != 0 && cell != ColorA){
								// a not yet explored cell
								if(cell == ColorB){ 
									board[i + dx][j + dy] = B;
									++B;
								}
								System.out.println(A + "," + cell + "," + 1);
							}
						}
					}
					++A;
				} else if(board[i][j] != 0){
					if(board[i][j] == ColorB){
						board[i][j] = B;
						++B;
					}
					
					System.out.println(board[i][j] + "," + sink + "," + 1);
				}
			}
		}
	}

	public void solve(Scanner input) {
		String coord;

		while (input.hasNextLine()) {
			// Construct board, then separate BLACK and WHITE cells
			coord = input.nextLine();
			int x = Integer.parseInt(coord.split(",")[0]);
			int y = Integer.parseInt(coord.split(",")[1]);
			
			if ((x + y) % 2 == 0){
				board[x][y] = -1;
				WHITE.add(coord);
			} else {
				board[x][y] = -2;
				BLACK.add(coord);
			}
		}
		output();
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		domino dm = new domino();
		dm.solve(sc);
	}
}



