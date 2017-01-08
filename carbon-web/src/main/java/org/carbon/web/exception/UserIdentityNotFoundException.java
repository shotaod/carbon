package org.carbon.web.exception;

/**
 * @author Shota Oda 2016/11/03.
 */
public class UserIdentityNotFoundException extends RuntimeException {
	public UserIdentityNotFoundException(String useraname) {
		super(useraname + "is not found");
	}
}
