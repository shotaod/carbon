package org.dabuntu.sample.exception;

/**
 * @author ubuntu 2016/10/08.
 */
public class ResouceNotFoundException extends RuntimeException {
	public ResouceNotFoundException(String message) {
		super(message);
	}

	public ResouceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
