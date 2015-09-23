package core;

import java.util.*;
import util.Coordinate;
import util.Ship;
import util.ShipPlacement;
import util.ShipPlacementException;

/**
 * Battleships class.
 * 
 * Fill in the methods. They are ordered in the order you should code them in.
 * 
 * NOTES: * Board states: * empty spot is denoted by a 0 ( e.g. char a = 0; ).
 * This is the default value for a char. * ship segments that are not hit are
 * denoted by a single lower case character that equals their ship handle. *
 * ship segments that are hit are denoted by a single UPPER case character that
 * equals their ship handle. * misses are denoted by a '.'
 * 
 * @author __Di Lu______
 * 
 */
public class BattleShips {

	GameConfiguration config;
	protected int gridHeight;
	protected int gridWidth;
	Player playerA;
	Player playerB;
	protected char[][] boardA;
	protected char[][] boardB;
	Collection<Ship> ships;
	int Counter = 0;
	int maxRun;

	/**
	 * #0 We advise you to implement this first
	 * 
	 * Constructor
	 * 
	 * Initialize all instance variables here.
	 * 
	 * @param config
	 *            - the configuration for this player
	 * @param player1
	 *            - one player
	 * @param player2
	 *            - other player
	 */
	public BattleShips(GameConfiguration config, Player player1, Player player2) {

		ships = config.getShips();
		this.config = config;
		playerA = player1;
		playerB = player2;
		gridHeight = config.getGridHeight();
		gridWidth = config.getGridWidth();
		boardA = new char[gridHeight][gridWidth];
		boardB = new char[gridHeight][gridWidth];
		maxRun = gridHeight * gridWidth;
		for (int i = 0; i < gridHeight; ++i) {
			for (int j = 0; j < gridWidth; ++j) {
				boardA[i][j] = 0;
				boardB[i][j] = 0;
			}
		}
	}

	/**
	 * #1 (implement this next)
	 * 
	 * Function to check if the coordinate is valid.
	 * 
	 * A coordinate is valid if and only if: * x value is within the board
	 * bounds (greater or equal to 0 and less than the grid width) * y value is
	 * within the board bounds (greater or equal to 0 and less than the grid
	 * height)
	 * 
	 * HINT: call this method to ensure the coordinate is valid before using it
	 * in any calculations!
	 * 
	 * @param x
	 *            - the x coordinate
	 * @param y
	 *            - the y coordinate
	 * @return true - coordinate is valid
	 * @return false - coordinate is invalid
	 */
	protected boolean isValidCoordinate(int x, int y) {
		// Return true or false depending on whether the coordinate is within
		// the map.
		return ((x >= 0 && x < gridWidth) && (y >= 0 && y < gridHeight));
	}

	/**
	 * #2
	 * 
	 * Function to check if the shot is valid.
	 * 
	 * A shot is valid if and only if: * it is within bounds
	 * 
	 * @param shot
	 *            - the shot being taken
	 * @return true - shot is valid
	 * @return false - the shot is invalid
	 */
	protected boolean isValidShot(Coordinate shot) {
		if (shot != null) {
			int shotX = shot.getX();
			int shotY = shot.getY();
			// invoke isValidCoordinate method above to check if it isVlidShot
			return (isValidCoordinate(shotX, shotY));
		} else {
			return false;
		}
	}

	/**
	 * #3
	 * 
	 * Function to get the value of the cell on a specific player's board
	 * 
	 * @param player
	 *            - the player whose board you are referring to (0 for player 1,
	 *            1 for player 2)
	 * 
	 * @param x
	 *            - the x coordinate
	 * @param y
	 *            - the y coordinate
	 * 
	 * @return the character in the cell (-1 if the coordinates are invalid)
	 */
	protected char getCellState(int player, int x, int y) {
		// First of all, clear away any invalid input.
		if (!isValidCoordinate(x, y) || (player != 0 && player != 1)) {
			return (char) -1;
		}
		// player'0' refers to the player 'A'.
		if (player == 0) {
			return boardA[y][x];
		} else {
			return boardB[y][x];
		}
	}

	/**
	 * #4
	 * 
	 * Function to resolve a shot.
	 * 
	 * It must: * change the correct board value to the correct value. * send
	 * the correct message to the correct player.
	 * 
	 * Correct value changes: * If the character is a lower case character -
	 * make it upper case (shot) * If the character is 0 - make it '.' (miss) *
	 * If the character is upper case or '.' - do nothing (miss)
	 * 
	 * Correct messages: * If the character is a lower case character -
	 * "HIT (x,y)" where x is the x coordinate and y is the y coordinate * If
	 * the whole ship has been sunk - "SUNK 'A'" where 'A' is the ship handle
	 * (don't include the ' marks) * If the character is 0 - "MISS (x,y)" where
	 * x is the x coordinate and y is the y coordinate * If the character is
	 * upper case or '.' - do nothing (miss)
	 * 
	 * @param shot
	 *            - the current shot
	 * @param player
	 *            - the player SHOOTING
	 */
	protected void resolveShot(Coordinate shot, int player) {
//
//		if (!isValidShot(shot) || player != 0 || player != 1) {
//			return;
//		} else {

			char[][] shooterBoard = null;
			char[][] defenderBoard = null;
			Player shooter = null;
			Player defender = null;

			// Identify shooter and defender, and x/y coordinates of shooting
			// point.
			if (player == 0) {
				shooterBoard = this.boardA;
				defenderBoard = this.boardB;
				shooter = this.playerA;
				defender = this.playerB;
			} else if (player == 1) {
				shooterBoard = this.boardB;
				defenderBoard = this.boardA;
				shooter = this.playerB;
				defender = this.playerA;
			}
			int shotX = shot.getX();
			int shotY = shot.getY();

			// See if the shooter has got a valid HIT.
			if (Character.isLowerCase(defenderBoard[shotY][shotX])) {
				shooter.notify("HIT (" + shotX + "," + shotY + ")");

				// If yes, then record the char of the defender's ship handle
				// and change its cell value to UpperCase
				char shipShotted = defenderBoard[shotY][shotX];
				defenderBoard[shotY][shotX] = Character
						.toUpperCase(defenderBoard[shotY][shotX]);

				/*
				 * Then iterate through the opponent's map to see if there
				 * exists any segments of this ship, if not, then tell both
				 * players that one battleship of defender is dead~
				 */
				int remains = 0;
				for (int i = 0; i < gridHeight; ++i) {
					for (int j = 0; j < gridWidth; ++j) {
						if (defenderBoard[i][j] == shipShotted) {
							++remains;
						}
					}
				}
				if (remains == 0) {
					shooter.notify("SUNK " + shipShotted);
					defender.notify("SUNK " + shipShotted);
				}

			} else if (defenderBoard[shotY][shotX] == 0) {
				/*
				 * If it is a Miss Shot, then notify the Shooter and change
				 * value of his board on that position to be '.'
				 */
				shooter.notify("MISS (" + shotX + "," + shotY + ")");
				defenderBoard[shotY][shotX] = '.';
			} else {
				// Otherwise keep cool.
				return;
			}
		}
//	}

	/**
	 * #5
	 * 
	 * Function to resolve a ship placement.
	 * 
	 * It must first check if a ship placement is valid, then place it if it is
	 * valid.
	 * 
	 * The placement of a ship is valid if and only if: * the shipPlacement is
	 * not null * no square that the ship is going to occupy is outside the grid
	 * * no square that the ship is going to occupy is already occupied by
	 * another ship
	 * 
	 * If a square of the ship is going to occupy a position that is already
	 * occupied by another ship, the already placed ship will sink and the new
	 * ship will not be placed.
	 * 
	 * If a ship is placed on top of multiple ships, they ALL sink.
	 * 
	 * @param shipPlacement
	 *            - the ship placement (starting coordinate and direction)
	 * @param ship
	 *            - the ship being placed
	 * @param player
	 *            - the player placing the ship
	 * @throws ShipPlacementException
	 *             - thrown when there is a placement problem
	 * @see ShipPlacementException
	 */
	protected void placeShip(Ship ship, ShipPlacement shipPlacement, int player)
			throws ShipPlacementException {

		/*
		 * If both the shipPlacement and ship are Null --- Do nothing; else: if
		 * the placement is not within the grid, throw the message to tell
		 * player "SHIP 'shipHandle' RAN AGROND"
		 */

		if (shipPlacement != null && ship != null) {
			if (!isValidCoordinate(shipPlacement.getBeginning().getX(),
					shipPlacement.getBeginning().getY())) {
				throw new ShipPlacementException("SHIP " + ship.getShipHandle()
						+ " RAN AGROUND");
			} else {

				// Declaration: the player's board, X/Y Coordinates of beginning
				// placement, and the length of the ship.
				char[][] board = null;
				if (player == 0) {
					board = this.boardA;
				} else if (player == 1) {
					board = this.boardB;
				}

				char shipHandle = ship.getShipHandle();
				int shipLength = ship.getShipLength();
				int X = shipPlacement.getBeginning().getX();
				int Y = shipPlacement.getBeginning().getY();

				// Make an ArrayList for to store multiple RUNOVER ships
				ArrayList<Ship> shipsRunOver = new ArrayList<Ship>();

				/*
				 * Detect the direction of the shipPlacement, and copy the ship
				 * segments according to directions, or if a invalid placement
				 * is found, do something to resolve that problem.
				 * 
				 * Condition Check: if the current cell is not valid (ie. not a
				 * '0' cell), then add the ship on that cell to the collection
				 * 'shipsRunOver'.
				 */
				switch (shipPlacement.getDirection()) {
				case NORTH: {
					for (int i = 0; i < shipLength; ++i) {
						if (getCellState(player, X, Y + i) == 0) {
							board[Y + i][X] = shipHandle;
						} else {
							// We only care about the 'ID' character of this
							// ship,
							// so
							// the length does not matter here(make it "1").
							shipsRunOver.add(new Ship(getCellState(player, X, Y
									+ i), 1));
						}
					}
					break;
				}
				case SOUTH: {
					for (int i = 0; i < shipLength; ++i) {
						if (getCellState(player, X, Y - i) == 0) {
							board[Y - i][X] = shipHandle;
						} else {
							shipsRunOver.add(new Ship(getCellState(player, X, Y
									- i), 1));
						}
					}
					break;
				}
				case EAST: {
					for (int i = 0; i < shipLength; ++i) {
						if (getCellState(player, X + i, Y) == 0) {
							board[Y][X + i] = shipHandle;
						} else {
							shipsRunOver.add(new Ship(getCellState(player, X
									+ i, Y), 1));
						}
					}
					break;
				}
				case WEST: {
					for (int i = 0; i < shipLength; ++i) {
						if (getCellState(player, X - i, Y) == 0) {
							board[Y][X - i] = shipHandle;
						} else {
							shipsRunOver.add(new Ship(getCellState(player, X
									- i, Y), 1));
						}
					}
					break;
				}
				}

				// Sovling more complex placement issues.
				String errorMessage = new String();
				/*
				 * If only the current ship only ran over one another ship,
				 * clear away both ship handles on the map and give a sensible
				 * warning.
				 * 
				 * Else if there are multiple ships that the ship being placed
				 * overlaps, use the 'iterator' to traverse elements in the
				 * ArrayList 'shipsRunOver' and add their shipHandles to an
				 * errorMessage string, and after that, remove all the ships
				 * involved in this accident.
				 */

				if (shipsRunOver.size() == 1) {
					for (int i = 0; i < gridHeight; ++i) {
						for (int j = 0; j < gridWidth; ++i) {
							if (board[i][j] == shipsRunOver.get(0)
									.getShipHandle()
									|| board[i][j] == shipHandle) {
								board[i][j] = 0;
							}
						}
					}
					throw new ShipPlacementException("SHIP " + shipHandle
							+ " RAN OVER SHIP "
							+ shipsRunOver.get(0).getShipHandle());

				} else if (shipsRunOver.size() > 1) {
					for (Ship s : shipsRunOver) {
						char temp = s.getShipHandle();
						errorMessage += temp + ",";
						for (int i = 0; i < gridHeight; ++i) {
							for (int j = 0; j < gridWidth; ++j) {
								if (board[i][j] == temp
										|| board[i][j] == shipHandle) {
									board[i][j] = 0;
								}
							}
						}
					}
					throw new ShipPlacementException("SHIP " + shipHandle
							+ " RAN OVER SHIP " + errorMessage);
				}
			}
		}else{
			return;
		}
	}

	/**
	 * #6
	 * 
	 * Function to get the enemy view of a player's board
	 * 
	 * You generate the view based on these rules: * if the value is upper case,
	 * show 'H' * if the value is lower case, show 0 * otherwise - the true
	 * value
	 * 
	 * @param player
	 *            - the player whose board you are going to view
	 * @return the view (contains only 'H', '.' and 0
	 */
	protected char[][] getEnemyView(int player) {

		char[][] enemyView = new char[gridHeight][gridWidth];
		char[][] enemyBoard = null;
		if (player == 0) {
			enemyBoard = this.boardA;
		} else if (player == 1) {
			enemyBoard = this.boardB;
		}

		for (int i = 0; i < gridHeight; ++i) {
			for (int j = 0; j < gridWidth; ++j) {
				if (Character.isUpperCase(enemyBoard[i][j])) {
					enemyView[i][j] = 'H';
				} else if (Character.isLowerCase(enemyBoard[i][j])) {
					enemyView[i][j] = 0;
				} else {
					enemyView[i][j] = enemyBoard[i][j];
				}
			}
		}
		return enemyView;
	}

	/**
	 * #7
	 * 
	 * Function to start and run the game.
	 * 
	 * @return the player that has won ( null if it was a draw )
	 */
	public Player run() {
		if (Counter == maxRun) {
			return null;
		}
		String namePA = playerA.getClass().getName();
		String namePB = playerB.getClass().getName();
		playerA.newGame(namePB, config);
		playerB.newGame(namePA, config);
		Player winner = null;

		for (Ship ship : ships) {
			try {
				placeShip(ship, playerA.getShipPlacement(ship, boardA), 0);
			} catch (ShipPlacementException e) {
				playerA.notify(e.getMessage());
			}

			try {
				placeShip(ship, playerB.getShipPlacement(ship, boardB), 1);
			} catch (ShipPlacementException e) {
				playerB.notify(e.getMessage());
			}

		}

		boolean gameOver = false;
		while (!gameOver) {
			// Request for next round of shots.
			playerA.notify("Shot your opponent ships!");
			playerB.notify("Shot your opponent ships!");

			// receive shots and resolve them.
			Coordinate nextShotPlayerA = playerA.getNextShot(boardA,
					getEnemyView(1));
			Coordinate nextShotPlayerB = playerB.getNextShot(boardB,
					getEnemyView(0));

			if (isValidShot(nextShotPlayerA) && isValidShot(nextShotPlayerB)) {
				resolveShot(nextShotPlayerA, 0);
				resolveShot(nextShotPlayerB, 1);
			} else {
				continue;
			}

			/*
			 * Note: 'tempX' is the number of segments remaining on the board
			 * for playerA or playerB.
			 * 
			 * If anyone has no more ships on the board, then his opponent
			 * player is the winner and set boolean value of gameOver to be
			 * TRUE.
			 */
			int tempA = 0;
			int tempB = 0;
			for (int i = 0; i < gridHeight; ++i) {
				for (int j = 0; j < gridWidth; ++j) {
					if (Character.isLowerCase(boardA[i][j])) {
						++tempA;
					}
					if (Character.isLowerCase(boardB[i][j])) {
						++tempB;
					}
				}
			}
			if (tempA == 0 && tempB != 0) {
				winner = playerB;
				playerB.notify("WIN");
				playerA.notify("LOSE");
				gameOver = true;

			} else if (tempB == 0 && tempA != 0) {
				winner = playerA;
				playerA.notify("WIN");
				playerB.notify("LOSE");
				gameOver = true;

			} else if (tempA == 0 && tempB == 0) {
				playerA.notify("DRAW");
				playerB.notify("DRAW");
				gameOver = true;

			}
		}
		++Counter;

		return winner;
	}
}
