package util;

/**
 * Do not alter this file in any way.
 */

public class ShipPlacement {
	public enum Direction{NORTH, SOUTH, EAST, WEST};
	
	Coordinate beginning;
	Direction direction;
	
	public ShipPlacement(Coordinate beginning, Direction direction){
		this.beginning = beginning;
		this.direction = direction;
	}
	
	public Coordinate getBeginning(){
		return beginning;
	}
	
	public Direction getDirection(){
		return direction;
	}
}
