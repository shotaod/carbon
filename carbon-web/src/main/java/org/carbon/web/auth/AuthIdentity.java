package org.carbon.web.auth;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthIdentity {
	String username();
	String cryptPassword();
	boolean confirm(String plainPassword);
}
