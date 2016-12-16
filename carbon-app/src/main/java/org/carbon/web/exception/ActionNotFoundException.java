package org.carbon.web.exception;

/**
 * @author ubuntu 2016/10/08.
 */
public class ActionNotFoundException extends RuntimeException {
	public ActionNotFoundException() {
		super();
	}
	public ActionNotFoundException(String message) {
		super(message);
	}
}
