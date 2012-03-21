package org.bitducks.spoofing.exception;

public class UnexpectedErrorException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnexpectedErrorException( Exception e, String message ) {
		super(message);
		this.addSuppressed(e);
	}
	
	public UnexpectedErrorException( String message ) {
		super(message);
	}

}
