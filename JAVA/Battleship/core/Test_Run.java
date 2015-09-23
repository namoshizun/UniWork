package core;

import java.util.ArrayList;
import java.util.Date;

import util.Ship;
import dilu3100.AI1119;
import dilu3100.Enlightenment;

public class Test_Run {

	public static void main(String[] args) {

		Date start = new Date();
		// Create the ships to test with (these are standard lengths)
		// Use whatever ships you want!

		ArrayList<Ship> ships = new ArrayList<Ship>();
		ships.add(new Ship('a', 5));
		ships.add(new Ship('b', 4));
		ships.add(new Ship('s', 3));
		ships.add(new Ship('d', 3));
		ships.add(new Ship('p', 2));

		// Test on a 10x10 board. Try other board sizes!
		GameConfiguration config = new GameConfiguration(10, 10, ships);

		// Create an instance of your player
		Player p1 = new AI1119();
		// Create another instance of your player
		Player p2 = new AI1119();

		// Create and run the game
		int p1Wins = 0;
		int p2Wins = 0;
		int maxTurn = 1000;
		int numOfCrash = 0;
		
		for (int i = 0; i < maxTurn; ++i) {
			try {
				BattleShips game = new BattleShips(config, p1, p2);
				Player winner = game.run();
				String winnerName = winner.getClass().getName();
				
				if (winnerName.equals("dilu3100.AI1119")) {
					++p1Wins;
				} else if (winnerName.equals("dilu3100.Enlightenment")) {
					++p2Wins;
				}
				if (i == maxTurn - 1) {
					if (p1Wins > p2Wins) {
						System.out.println("Winner is AI1119");
					} else {
						System.out.println("Winner is Enlightenment");
					}
					System.out.println("Crash " + numOfCrash + " times");
					Date end = new Date();
					System.out.println("Time taken is "
							+ (end.getTime() - start.getTime()) + "ms");
				}
			} catch (Exception e) {
				++numOfCrash;
			}
		}
	}
}
