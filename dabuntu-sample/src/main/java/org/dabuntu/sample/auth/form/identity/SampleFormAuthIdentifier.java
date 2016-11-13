package org.dabuntu.sample.auth.form.identity;

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
public class SampleFormAuthIdentifier implements AuthIdentifier<SampleFormAuthIdentity> {

	@Inject
	private UserRoleService userRoleService;

	@Override
	public Class<SampleFormAuthIdentity> getType() {
		return SampleFormAuthIdentity.class;
	}

	@Override
	public SampleFormAuthIdentity find(String username) throws UserIdentityNotFoundException{
		return Optional.ofNullable(userRoleService.findByUsername(username))
				.map(SampleFormAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(username));
	}
}
