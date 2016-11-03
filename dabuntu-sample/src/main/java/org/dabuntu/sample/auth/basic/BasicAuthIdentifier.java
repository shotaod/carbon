package org.dabuntu.sample.auth.basic;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.sample.repository.User;
import org.dabuntu.web.auth.AuthIdentifier;
import org.dabuntu.web.exception.UserIdentityNotFoundException;

import java.util.Arrays;
import java.util.List;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class BasicAuthIdentifier implements AuthIdentifier<BasicAuthIdentity> {

	private List<User> users = Arrays.asList(
			new User("ubuntu", "hoge@hoge", "password"),
			new User("ubun2", "hoge@hoge", "password"),
			new User("ubun3", "hoge@hoge", "password"),
			new User("ubun4", "hoge@hoge", "password")
	);

	@Override
	public BasicAuthIdentity find(String username) throws UserIdentityNotFoundException{
		return users.stream()
				.filter(user -> user.getUsername().equals(username))
				.findFirst()
				.map(BasicAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(username));
	}
}
