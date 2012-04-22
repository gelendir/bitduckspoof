package org.bitducks.spoofing.exception;

/**
 * Wrapper exception used throughout the project.
 * This exception is used for convenience.
 * Instead of rethrowing an exception up through
 * the whole call stack, we throw a runtime exception.
 * It gives more flexibility when we want to modify an exception
 * hierarchy, and helps to avoid modifying a whole call stack
 * when modifying an exception from a low-level abstraction.
 * 
 * @author Gregory Eric Sanderson <gzou2000@gmail.com>
 *
 */
public class UnexpectedErrorException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor. Creates a new exception.
	 * Used when we want to wrap an exception that we received
	 * from somewhere else.
	 * 
	 * @param e The original exception that was raised
	 * @param message A message to accompany the exception
	 */
	public UnexpectedErrorException( Exception e, String message ) {
		super(message);
		this.addSuppressed(e);
	}
	
	/**
	 * Constructor. Creates an exception.
	 * @param message Message associated to the exception.
	 */
	public UnexpectedErrorException( String message ) {
		super(message);
	}

}
