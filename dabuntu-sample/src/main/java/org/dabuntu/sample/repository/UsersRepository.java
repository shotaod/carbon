package org.dabuntu.sample.repository;

import org.dabuntu.component.annotation.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author ubuntu 2016/11/04.
 */
@Component
public class UsersRepository {
	private List<User> data = Arrays.asList(
			new User("ubuntu", "hoge@hoge", "password"),
			new User("ubun2", "hoge@hoge", "password"),
			new User("ubun3", "hoge@hoge", "password"),
			new User("ubun4", "hoge@hoge", "password")
	);

	public Optional<User> findByUsername(String username) {
		return data.stream()
			.filter(user -> user.getUsername().equals(username))
			.findFirst();
	}
}
