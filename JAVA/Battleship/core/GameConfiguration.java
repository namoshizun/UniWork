package core;

/**
 * Do not alter this file in any way.
 */

import java.util.Collection;

import util.Ship;

public class GameConfiguration {
	
	private int gridWidth;
	private int gridHeight;
	
	private Collection<Ship> ships;
	
	public GameConfiguration(int gridWidth, int gridHeight, 
			Collection<Ship> ships){
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		this.ships = ships;
	}

	public int getGridWidth() {
		return gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public Collection<Ship> getShips() {
		return ships;
	}
}
