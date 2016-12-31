package org.carbon.component.exception;

/**
 * @author Shota Oda 2016/10/02
 */
public class ClassNotRegisteredException extends RuntimeException {
	public ClassNotRegisteredException(String message) {
		super(message);
	}
	public ClassNotRegisteredException(String message, Throwable cause) {
		super(message, cause);
	}
}
