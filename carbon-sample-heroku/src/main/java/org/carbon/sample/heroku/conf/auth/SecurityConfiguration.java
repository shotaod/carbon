package org.carbon.sample.heroku.conf.auth;

import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.conf.auth.identity.HerokuAuthEventListener;
import org.carbon.sample.heroku.conf.auth.identity.HerokuAuthIdentifier;
import org.carbon.authentication.AuthConfigAdapter;
import org.carbon.authentication.AuthDefinition;
import org.carbon.sample.heroku.conf.auth.identity.HerokuAuthIdentity;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/12.
 */
@Configuration
public class SecurityConfiguration implements AuthConfigAdapter {
    @Inject
    private HerokuAuthIdentifier authIdentifier;
    @Inject
    private HerokuAuthRequestMapper requestMapper;
    @Inject
    private HerokuAuthEventListener authEvent;

    @Override
    public void configure(AuthDefinition config) {
        config
            .<HerokuAuthIdentity>define()
                .base("/user", "/task")
                .redirect("/user/login")
                .endPoint(HttpMethod.POST, "/user/login")
                .logout("/user/logout")
                .permitGetAll("/user/login", "/user/signup",  "/task/about", "/static/**")
                .permit(HttpMethod.POST, "/user/signup")
                .requestMapper(requestMapper)
                .identifier(authIdentifier)
                .eventListener(authEvent)
            .end()
        ;
    }
}
