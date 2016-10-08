package org.dabuntu.web.exception;

/**
 * @author ubuntu 2016/10/08.
 */
public class ActionInvokeException extends RuntimeException {
	public ActionInvokeException(String message) {
		super(message);
	}
	public ActionInvokeException(String message, Throwable cause) {
		super(message, cause);
	}
}
