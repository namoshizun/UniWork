package dilu3100;

import java.util.ArrayList;
import util.Coordinate;
import util.Ship;
import util.ShipPlacement;
import util.ShipPlacement.Direction;
import core.GameConfiguration;
import core.Player;

/**
 * AI1119 - BattleShips Player
 * 
 * @author Chenrui Liu
 * @version 1.0 June-02-2014
 * 
 */
public class AI1119 implements Player {
	// Configuration var
	GameConfiguration config;
	// Attack var
	ArrayList<Coordinate> amiList1 = new ArrayList<Coordinate>();
	ArrayList<Coordinate> amiList2 = new ArrayList<Coordinate>();
	ArrayList<Coordinate> hits = new ArrayList<Coordinate>();

	Coordinate lastShot;

	int counterWidth;
	int placeCounter = -2;

	Direction dir = Direction.NORTH;

	@Override
	public void newGame(String opponent, GameConfiguration config) {
		this.config = config;

		counterWidth = config.getGridWidth();

		for (int i = 0; i < config.getGridWidth(); i += 1) {
			for (int j = 0; j < config.getGridHeight(); j += 1) {
				if (((i + j) & 1) == 0) {
					amiList1.add(new Coordinate(i, j));
				} else {
					amiList2.add(new Coordinate(i, j));
				}
			}
		}
	}

	@Override
	public ShipPlacement getShipPlacement(Ship ship, char[][] myBoard) {
		skipedNum();
		int xcoord = placeCounter;
		int ycoord;
		for (int count = 0; count < 10; count += 1) { // Limited trials avoiding timeout
			ycoord = (int) (Math.random() * (config.getGridHeight() - ship
					.getShipLength()));
			if (!overlap(myBoard, xcoord, ycoord, ship.getShipLength())) {
				return new ShipPlacement(new Coordinate(xcoord, ycoord),
						Direction.NORTH);
			}
		}
		return null;
	}

	private boolean overlap(char[][] board, int x, int y, int shiplength) {
		for (int i = 0; i < shiplength; i += 1) {
			if (board[x][y + i] != 0) {
				return true;
			}
		}
		return false;
	}

	private void skipedNum() {
		if (placeCounter < counterWidth - 2) {
			placeCounter += 2;
		} else {
			placeCounter -= ((int) ((counterWidth - 1) / 2) * 2 - 1);
		}
	}

	@Override
	public Coordinate getNextShot(char[][] myBoard, char[][] myViewOfOpponentBoard) {
		for (int count = 0; count < 10; count += 1) { // Limited trials avoiding timeout
			if (hits.size() != 0) {
				for (Coordinate i : hits) {
					if (i.getX() > 0 && myViewOfOpponentBoard[i.getY()][i.getX() - 1] == 0) {
						return popCoord(new Coordinate(i.getX() - 1, i.getY()));
					} else if (i.getX() < (config.getGridWidth() - 1) && myViewOfOpponentBoard[i.getY()][i.getX() + 1] == 0) {
						return popCoord(new Coordinate(i.getX() + 1, i.getY()));
					} else if (i.getY() > 0 && myViewOfOpponentBoard[i.getY() - 1][i.getX()] == 0) {
						return popCoord(new Coordinate(i.getX(), i.getY() - 1));
					} else if (i.getY() < (config.getGridHeight() - 1) && myViewOfOpponentBoard[i.getY() + 1][i.getX()] == 0) {
						return popCoord(new Coordinate(i.getX(), i.getY() + 1));
					} else if (hits.indexOf(i) == hits.size()-1){
						hits.remove(i);
						break;
					}
				}
			} else {
				ArrayList<Coordinate> theList;
				if (amiList1.size() != 0) {
					theList = amiList1;
				} else {
					theList = amiList2;
				}
				return popCoord(theList.get((int) (Math.random() * theList
						.size())));
			}
		}
		return null;
	}

	private Coordinate popCoord(Coordinate coord) {
		if ((coord.getX() + coord.getY() & 1) == 0) {
			amiList1.remove(coord);
		} else {
			amiList2.remove(coord);
		}
		lastShot = coord;
		return coord;
	}

	@Override
	public void notify(String message) {
		if (message.contains("HIT")) {
			hits.add(new Coordinate(Integer.parseInt(message.substring(5, 6)),
					Integer.parseInt(message.substring(7, 8))));
		}
	}
}
