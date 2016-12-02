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
public class FormAuthIdentifier implements AuthIdentifier<FormAuthIdentity> {

	@Inject
	private UserRoleService userRoleService;

	@Override
	public Class<FormAuthIdentity> getType() {
		return FormAuthIdentity.class;
	}

	@Override
	public FormAuthIdentity find(String username) throws UserIdentityNotFoundException{
		return Optional.ofNullable(userRoleService.findByUsername(username))
				.map(FormAuthIdentity::new)
				.orElseThrow(() -> new UserIdentityNotFoundException(username));
	}
}
