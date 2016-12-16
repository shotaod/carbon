package org.carbon.component.exception;

/**
 * @author Shota Oda 2016/10/02
 */
public class ClassNotRegisterdException extends RuntimeException {
	public ClassNotRegisterdException(String message) {
		super(message);
	}
	public ClassNotRegisterdException(String message, Throwable cause) {
		super(message, cause);
	}
}
