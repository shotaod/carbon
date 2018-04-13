package org.carbon.sample.auth.form.identity;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.domain.service.UserRoleService;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class FormAuthIdentifier implements AuthIdentifier<FormAuthIdentity> {

    @Assemble
    private UserRoleService userRoleService;

    @Override
    public Optional<FormAuthIdentity> identify(String identity) {
        return Optional.ofNullable(userRoleService.findByUsername(identity))
                .map(FormAuthIdentity::new);
    }
}
