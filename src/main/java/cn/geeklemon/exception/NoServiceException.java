package cn.geeklemon.exception;

public class NoServiceException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private String other;
	public NoServiceException(String message) {
		this.message = message;
	}
	@Override
	public String getMessage() {
		return message;
	}
	
}
