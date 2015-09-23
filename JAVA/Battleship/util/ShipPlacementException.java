package util;

/**
 * Do not alter this file in any way.
 */

/**
 * Function used to throw a ship placement exception
 * 
 * Will be thrown if and only if:
 * 		* the ship being placed has any of its sections out of bounds.
 * 		* the ship being placed is placed over any other ships
 * 
 * The messages that will be given are:
 * 		* "SHIP 'A' RAN AGROUND" where 'A' denotes a ship handle
 * 		* "SHIP 'A' RAN OVER SHIP 'B',...,'Z'" where 'A', 'B', 'Z' denote ship handles and '...' denotes one or more
 * 
 * NOTE: do not include the single quotation marks in the messages.
 * 
 */
public class ShipPlacementException extends Throwable{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public ShipPlacementException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}

}
