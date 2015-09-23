package util;

/**
 * Do not alter this file in any way.
 */

public class Ship {
	private char shipHandle;
	private int length;
	
	public Ship(char shipHandle, int length){
		this.shipHandle = shipHandle;
		this.length = length;
	}
	
	public char getShipHandle(){
		return shipHandle;
	}
	
	public int getShipLength(){
		return length;
	}
}
