package org.carbon.web.exception;

/**
 * @author Shota Oda 2016/10/08.
 */
public class ActionMappingException extends RuntimeException {
	public ActionMappingException() {
		super();
	}
	public ActionMappingException(String message) {
		super(message);
	}
}
