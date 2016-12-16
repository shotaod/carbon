package org.dabuntu.sample.auth.form.identity;

import org.dabunt.sample.tables.pojos.User;
import org.dabuntu.web.auth.AuthIdentity;

/**
 * @author ubuntu 2016/11/03.
 */
public class FormAuthIdentity implements AuthIdentity {

	private User user;

	public FormAuthIdentity(User user) {
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
