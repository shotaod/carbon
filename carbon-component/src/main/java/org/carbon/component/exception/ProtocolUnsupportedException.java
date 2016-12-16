package org.carbon.component.exception;

/**
 * @author Shota Oda 2016/11/16.
 */
public class ProtocolUnsupportedException extends RuntimeException{
	public ProtocolUnsupportedException() {
		super();
	}
	public ProtocolUnsupportedException(String message) {
		super(message);
	}
}
