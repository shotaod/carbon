package org.carbon.web.exception;

/**
 * @author Shota Oda 2016/10/08.
 */
public class ActionInvokeException extends RuntimeException {
	public ActionInvokeException(String message) {
		super(message);
	}
	public ActionInvokeException(String message, Throwable cause) {
		super(message, cause);
	}
}
