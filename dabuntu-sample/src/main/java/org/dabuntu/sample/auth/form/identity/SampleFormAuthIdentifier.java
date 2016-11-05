package org.dabuntu.sample.auth.form.identity;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.repository.UsersRepository;
import org.dabuntu.web.auth.AuthIdentifier;
import org.dabuntu.web.exception.UserIdentityNotFoundException;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class SampleFormAuthIdentifier implements AuthIdentifier<SampleFormAuthIdentity> {

	@Inject
	private UsersRepository usersRepository;

	@Override
	public Class<SampleFormAuthIdentity> getType() {
		return SampleFormAuthIdentity.class;
	}

	@Override
	public SampleFormAuthIdentity find(String username) throws UserIdentityNotFoundException{
		return usersRepository.findByUsername(username)
				.map(SampleFormAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(username));
	}
}
