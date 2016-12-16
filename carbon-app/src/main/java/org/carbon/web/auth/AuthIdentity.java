package org.carbon.web.auth;

/**
 * @author ubuntu 2016/11/03.
 */
public interface AuthIdentity {
	String username();
	String cryptPassword();
	boolean confirm(String plainPassword);
}
