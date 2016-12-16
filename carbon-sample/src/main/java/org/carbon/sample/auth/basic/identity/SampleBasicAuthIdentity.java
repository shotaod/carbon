package org.carbon.sample.auth.basic.identity;

import org.carbon.sample.tables.pojos.User;
import org.carbon.web.auth.AuthIdentity;

/**
 * @author Shota Oda 2016/11/03.
 */
public class SampleBasicAuthIdentity implements AuthIdentity {

	private User user;

	public SampleBasicAuthIdentity(User user) {
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
