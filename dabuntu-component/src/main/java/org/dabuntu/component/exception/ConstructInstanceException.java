package org.dabuntu.component.exception;

/**
 * @author ubuntu 2016/11/28.
 */
public class ConstructInstanceException extends RuntimeException {
	public ConstructInstanceException(String message) {
		super(message);
	}
	public ConstructInstanceException(String message, Throwable cause) {
		super(message, cause);
	}
}
