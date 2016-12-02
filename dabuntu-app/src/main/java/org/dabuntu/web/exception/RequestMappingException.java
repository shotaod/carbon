package org.dabuntu.web.exception;

/**
 * @author ubuntu 2016/10/12.
 */
public class RequestMappingException extends RuntimeException {
	public RequestMappingException(String message, Throwable cause) {
		super(message, cause);
	}
	public RequestMappingException(String message) {
		super(message);
	}
}
