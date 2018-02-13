package org.carbon.sample.auth.form.identity;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.domain.service.UserRoleService;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class FormAuthIdentifier implements AuthIdentifier<FormAuthIdentity> {

    @Inject
    private UserRoleService userRoleService;

    @Override
    public Optional<FormAuthIdentity> find(String identity) {
        return Optional.ofNullable(userRoleService.findByUsername(identity))
                .map(FormAuthIdentity::new);
    }
}
