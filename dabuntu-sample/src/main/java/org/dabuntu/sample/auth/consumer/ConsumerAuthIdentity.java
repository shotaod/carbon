package org.dabuntu.sample.auth.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dabunt.sample.tables.pojos.Student;
import org.dabuntu.web.auth.AuthIdentity;

/**
 * @author ubuntu 2016/11/23.
 */
@Data
@AllArgsConstructor
public class ConsumerAuthIdentity implements AuthIdentity{

	private Student student;

	@Override
	public String username() {
		return student.getUsername();
	}

	@Override
	public String cryptPassword() {
		return student.getPassword();
	}

	@Override
	public boolean confirm(String plainPassword) {
		return cryptPassword().equals(plainPassword);
	}
}
