/**
 * 
 */
package edu.ucsd.som.vchs.medgrp.revenue.auth;

/**
 * @author somdev5
 *
 */
public class SecurityAccessException extends RuntimeException {

	/**
	 * 
	 */
	public SecurityAccessException() {
		super("The user does not have access to this division");
	}

	/**
	 * @param message
	 */
	public SecurityAccessException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SecurityAccessException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SecurityAccessException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SecurityAccessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
