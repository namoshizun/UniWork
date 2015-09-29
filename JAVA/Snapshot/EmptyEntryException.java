package spdb;

public class EmptyEntryException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public EmptyEntryException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
