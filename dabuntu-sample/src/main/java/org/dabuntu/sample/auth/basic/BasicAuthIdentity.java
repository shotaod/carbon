package org.dabuntu.sample.auth.basic;

import org.dabuntu.sample.repository.User;
import org.dabuntu.web.auth.AuthIdentity;

/**
 * @author ubuntu 2016/11/03.
 */
public class BasicAuthIdentity implements AuthIdentity {

	private User user;

	public BasicAuthIdentity(User user) {
		this.user = user;
	}

	@Override
	public String username() {
		return user.getUsername();
	}

	@Override
	public String cryptPassword() {
		return user.getEncPassword();
	}

	@Override
	public boolean confirm(String plainPassword) {
		return cryptPassword().equals(plainPassword);
	}
}
