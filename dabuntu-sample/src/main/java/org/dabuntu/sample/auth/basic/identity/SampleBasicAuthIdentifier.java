package org.dabuntu.sample.auth.basic.identity;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.sample.domain.service.UserRoleService;
import org.dabuntu.web.auth.AuthIdentifier;
import org.dabuntu.web.exception.UserIdentityNotFoundException;

import java.util.Optional;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class SampleBasicAuthIdentifier implements AuthIdentifier<SampleBasicAuthIdentity> {

	@Inject
	private UserRoleService userRoleService;

	@Override
	public Class<SampleBasicAuthIdentity> getType() {
		return SampleBasicAuthIdentity.class;
	}

	@Override
	public SampleBasicAuthIdentity find(String username) throws UserIdentityNotFoundException{
		return Optional.ofNullable(userRoleService.findByUsername(username))
				.map(SampleBasicAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(username));
	}
}
