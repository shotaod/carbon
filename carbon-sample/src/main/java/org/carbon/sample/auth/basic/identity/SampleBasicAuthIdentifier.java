package org.carbon.sample.auth.basic.identity;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.domain.service.UserRoleService;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class SampleBasicAuthIdentifier implements AuthIdentifier<SampleBasicAuthIdentity> {

    @Assemble
    private UserRoleService userRoleService;

    @Override
    public Optional<SampleBasicAuthIdentity> identify(String identity) {
        return Optional.ofNullable(userRoleService.findByUsername(identity))
                .map(SampleBasicAuthIdentity::new);
    }
}
