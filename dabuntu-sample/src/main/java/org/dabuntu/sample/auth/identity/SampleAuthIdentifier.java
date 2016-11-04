package org.dabuntu.sample.auth.identity;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.repository.UsersRepository;
import org.dabuntu.web.auth.AuthIdentifier;
import org.dabuntu.web.exception.UserIdentityNotFoundException;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class SampleAuthIdentifier implements AuthIdentifier<SampleAuthIdentity> {

	@Inject
	private UsersRepository usersRepository;

	@Override
	public SampleAuthIdentity find(String username) throws UserIdentityNotFoundException{
		return usersRepository.findByUsername(username)
				.map(SampleAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(username));
	}
}
