package org.dabuntu.sample.auth.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dabunt.sample.tables.pojos.Lecturer;
import org.dabuntu.web.auth.AuthIdentity;

/**
 * @author ubuntu 2016/11/23.
 */
@Data
@AllArgsConstructor
public class BusinessAuthIdentity implements AuthIdentity{

	private Lecturer user;

	@Override
	public String username() {
		return user.getUserName();
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
