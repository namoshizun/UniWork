package dilu3100;

import java.util.ArrayList;
import java.util.Random;

import util.Coordinate;
import util.Ship;
import util.ShipPlacement;
import util.ShipPlacement.Direction;
import core.GameConfiguration;
import core.Player;

public class Enlightenment implements Player {

	GameConfiguration config;
	int width;
	int height;
	Coordinate shot = null;
	// The shotRecord will record the coordinate of each shot.
	ArrayList<Coordinate> shotRecord = new ArrayList<Coordinate>();

	@Override
	public Coordinate getNextShot(char[][] myBoard,
			char[][] myViewOfOpponentBoard) {

		// Take a random shot just for the first turn.
		if (shotRecord.size() == 0) {
			shot = new Coordinate(new Random().nextInt(width),
					new Random().nextInt(height));
			shotRecord.add(shot);
			return shot;
		}

		// And for later rounds, each time get the last shot coordinate to be a
		// reference before taking the next shot.
		Coordinate lastShot = new Coordinate(shotRecord.get(
				shotRecord.size() - 1).getX(), shotRecord.get(
				shotRecord.size() - 1).getY());

		/*
		 * If the last shot is a direct HIT, then define that the next shot will
		 * be aiming its surrounding area because it is highly possible that
		 * another ship segment is around. As the nextShot coordinate is
		 * randonmly generated, either the shot is on the same spot or a bombed
		 * spot, go back and generate a new one.
		 */
		if (myViewOfOpponentBoard[lastShot.getY()][lastShot.getX()] == 'H') {
			Random r = new Random();

			while (true) {
				shot = new Coordinate(lastShot.getX() + (r.nextInt(3) - 1),
						lastShot.getY() + (r.nextInt(3) - 1));
				if (shot.getX() < 0 || shot.getY() < 0 || shot.getX() >= width
						|| shot.getY() >= height) {
					continue;
				} else {
					if ((shot.getX() == lastShot.getX() && shot.getY() == lastShot
							.getY())
							|| myViewOfOpponentBoard[shot.getY()][shot.getX()] != 0) {
						continue;
					} else {
						shotRecord.add(shot);
						break;
					}
				}
			}

			// If the lastShot is not a HIT, then just go through the enemy
			// board and pick a void spot.
		} else {
			label: for (int i = 0; i < height; ++i) {
				for (int j = 0; j < width; ++j) {
					if (myViewOfOpponentBoard[i][j] == 0) {
						shot = new Coordinate(j, i);
						shotRecord.add(shot);
						break label;
					}
				}
			}
		}
		return shot;
	}

	@Override
	public ShipPlacement getShipPlacement(Ship ship, char[][] myBoard) {

		// Initialize all variables.
		ShipPlacement placement = null;
		Random r = new Random();
		Coordinate begin;
		Direction direction;
		int length = ship.getShipLength();
		boolean placed = false;

		/*
		 * Random generate a direction (NORTH,SOUTH,WEST OR EAST), {Then
		 * according to the direction, under the condition that the ship is
		 * still within the board, random generate a beginning coordinate. And
		 * after that, check if any other ships would be overlapped after this
		 * entire ship is placed, if so, then go back and generate a new
		 * beginning coordinate.}
		 */
		label: while (!placed) {
			direction = Direction.values()[r.nextInt(3) + 1];
			switch (direction) {
			case NORTH: {
				begin = new Coordinate(r.nextInt(width), r.nextInt(height
						- length));
				placement = new ShipPlacement(begin, direction);
				for (int i = 0; i < length; ++i) {
					if (myBoard[begin.getY() + i][begin.getX()] != 0) {
						continue label;
					}
				}
				return placement;
			}
			case SOUTH: {
				begin = new Coordinate(r.nextInt(width), length
						+ r.nextInt(height - length));
				placement = new ShipPlacement(begin, direction);
				for (int i = 0; i < length; ++i) {
					if (myBoard[begin.getY() - i][begin.getX()] != 0) {
						continue label;
					}
				}
				return placement;
			}
			case WEST: {
				begin = new Coordinate(length + r.nextInt(width - length),
						r.nextInt(height));
				placement = new ShipPlacement(begin, direction);
				for (int i = 0; i < length; ++i) {
					if (myBoard[begin.getY()][begin.getX() - i] != 0) {
						continue label;
					}
				}
				return placement;
			}
			case EAST: {
				begin = new Coordinate(r.nextInt(width - length),
						r.nextInt(height));
				placement = new ShipPlacement(begin, direction);
				for (int i = 0; i < length; ++i) {
					if (myBoard[begin.getY()][begin.getX() + i] != 0) {
						continue label;
					}
				}
				return placement;
			}
			}
		}
		return null;
	}

	@Override
	public void notify(String message) {
		// YES IT IS DOING NOTHING BECAUSE IT IS SO TIME CONSUMING TO DEVELOP A
		// GOOD AI, I MEAN JUST FOR A CODE BEGINNER ...
	}

	@Override
	public void newGame(String opponent, GameConfiguration config) {
		this.config = config;
		width = config.getGridWidth();
		height = config.getGridHeight();

		notify("Your are playing with " + opponent);
		notify("The mapSize is " + config.getGridHeight() + "*"
				+ config.getGridHeight());
		notify("Please place your ship within the board. ");
	}
}
