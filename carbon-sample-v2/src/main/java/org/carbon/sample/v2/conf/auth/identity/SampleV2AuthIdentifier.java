package org.carbon.sample.v2.conf.auth.identity;

import java.util.Optional;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.sample.v2.app.user.UserService;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class SampleV2AuthIdentifier implements AuthIdentifier<SampleV2AuthIdentity> {

    @Assemble
    private UserService userService;

    @Override
    public Optional<SampleV2AuthIdentity> identify(String identity) {
        return userService.findByEmail(identity)
                .map(SampleV2AuthIdentity::new);
    }
}
