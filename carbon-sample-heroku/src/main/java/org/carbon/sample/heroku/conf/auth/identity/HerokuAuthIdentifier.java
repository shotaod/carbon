package org.carbon.sample.heroku.conf.auth.identity;

import org.carbon.authentication.AuthIdentifier;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.web.user.UserService;
import org.carbon.web.exception.UserIdentityNotFoundException;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class HerokuAuthIdentifier implements AuthIdentifier<HerokuAuthIdentity> {

    @Inject
    private UserService userService;

    @Override
    public Class<HerokuAuthIdentity> getType() {
        return HerokuAuthIdentity.class;
    }

    @Override
    public HerokuAuthIdentity find(String username) throws UserIdentityNotFoundException{
        return userService.findByEmail(username)
                .map(HerokuAuthIdentity::new)
                .orElseThrow(() -> new UserIdentityNotFoundException(username));
    }
}
