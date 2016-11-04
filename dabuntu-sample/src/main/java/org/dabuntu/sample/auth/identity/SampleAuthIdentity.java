package org.dabuntu.sample.auth.identity;

import org.dabuntu.sample.repository.User;
import org.dabuntu.web.auth.AuthIdentity;

/**
 * @author ubuntu 2016/11/03.
 */
public class SampleAuthIdentity implements AuthIdentity {

	private User user;

	public SampleAuthIdentity(User user) {
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
