package org.carbon.sample.auth.basic.identity;

import org.carbon.sample.domain.service.UserRoleService;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.auth.AuthIdentifier;
import org.carbon.web.exception.UserIdentityNotFoundException;

import java.util.Optional;

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
    public SampleBasicAuthIdentity find(String username) throws UserIdentityNotFoundException{
        return Optional.ofNullable(userRoleService.findByUsername(username))
                .map(SampleBasicAuthIdentity::new)
                .orElseThrow(() -> new UserIdentityNotFoundException(username));
    }
}
