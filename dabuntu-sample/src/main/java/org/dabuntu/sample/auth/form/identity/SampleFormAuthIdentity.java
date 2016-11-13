package org.dabuntu.sample.auth.form.identity;

import org.dabuntu.sample.domain.entity.User;
import org.dabuntu.web.auth.AuthIdentity;

/**
 * @author ubuntu 2016/11/03.
 */
public class SampleFormAuthIdentity implements AuthIdentity {

	private User user;

	public SampleFormAuthIdentity(User user) {
		this.user = user;
	}

	@Override
	public String username() {
		return user.getUsername();
	}

	@Override
	public String cryptPassword() {
		return user.getPassword();
	}

	@Override
	public boolean confirm(String plainPassword) {
		return cryptPassword().equals(plainPassword);
	}
}
