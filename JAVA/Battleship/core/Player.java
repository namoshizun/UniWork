package core;

/**
 * Do not alter this file in any way.  
 * You need to make a class that implements this interface, and it must be 
 * in a package that is named your unikey.
 * 
 * For the BattleShips League, your Player's methods must complete execution 
 * in these times:
 * 	Constructor: 10ms
 * 	newGame: 5ms
 * 	getNextShot: 3ms
 * 	getShipPlacement: 3ms
 * If the Constructor or the newGame methods take too long then your player
 * forfeits that game.
 * If the getNextShot or getShipPlacement methods take too long then the 
 * ship placement or shot will be ignored.
 * These time constraints are so that the large number of games can be run 
 * in a reasonable amount of time every hour. 
 */

import util.Coordinate;
import util.Ship;
import util.ShipPlacement;

public interface Player {
	/**
	 * Get the next proposed shot of the player
	 * @return
	 */
	public Coordinate getNextShot(char[][] myBoard, 
			char[][] myViewOfOpponentBoard);
	
	/**
	 * Get the ship placement
	 * @param ship the ship being placed
	 * @param myBoard the current state of my board
	 * @return 
	 */
	public ShipPlacement getShipPlacement(Ship ship, char[][] myBoard);
	
	/**
	 * This is where the player is notified of a message by the game.
	 * Can be one of these messages:
	 * 		"HIT (x,y)"
	 * 			where x, y are coordinate values
	 * 		"MISS"
	 * 		"SUNK X" 
	 * 			where X is the ship handle
	 * 		"WIN"
	 * 		"LOSE"
	 * 		"DRAW"
	 * 		"Ship X RAN AGROUND"
	 * 			where X is the ship handle
	 * 		"SHIP X  RAN OVER SHIP Y" 
	 * 			where X and Y are different ship handles 
	 * @param message
	 */
	public void notify(String message);

	/**
	 * Notify player of a new game
	 * 
	 * @param opponent - the opponent
	 * @param config - the game configuration (details)
	 */
	void newGame(String opponent, GameConfiguration config);
}
