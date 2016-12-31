package org.carbon.sample.auth.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.carbon.sample.tables.pojos.Lecturer;
import org.carbon.web.auth.AuthIdentity;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota Oda 2016/11/23.
 */
@Data
@AllArgsConstructor
public class BusinessAuthIdentity implements AuthIdentity{

	private Lecturer user;

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
        return BCrypt.checkpw(plainPassword, cryptPassword());
	}
}
