package org.carbon.sample.auth.form.identity;

import org.carbon.sample.domain.service.UserRoleService;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.auth.AuthIdentifier;
import org.carbon.web.exception.UserIdentityNotFoundException;

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
