package org.carbon.sample.auth.basic.identity;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.domain.service.UserRoleService;
import org.carbon.web.exception.UserIdentityNotFoundException;

/**
 * @author Shota Oda 2016/11/03.
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
    public SampleBasicAuthIdentity find(String username) throws UserIdentityNotFoundException {
        return Optional.ofNullable(userRoleService.findByUsername(username))
                .map(SampleBasicAuthIdentity::new)
                .orElseThrow(() -> new UserIdentityNotFoundException(username));
    }
}
