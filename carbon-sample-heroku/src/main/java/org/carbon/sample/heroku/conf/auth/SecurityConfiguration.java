package org.carbon.sample.heroku.conf.auth;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.conf.auth.identity.HerokuAuthFinisher;
import org.carbon.sample.heroku.conf.auth.identity.HerokuAuthIdentifier;
import org.carbon.web.auth.SecurityConfigAdapter;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/12.
 */
@Component
public class SecurityConfiguration implements SecurityConfigAdapter {
    @Inject
    private HerokuAuthIdentifier authIdentifier;
    @Inject
    private HetorkuAuthRequestMapper requestMapper;
    @Inject
    private HerokuAuthFinisher authFinisher;
    @Override
    public void configure(org.carbon.web.auth.SecurityConfiguration config) {
        config.define()
            .base("/sample/security")
            .redirect("/sample/security/login")
            .endPoint(HttpMethod.POST, "/sample/security/login")
            .logout("/sample/security/logout")
            .permitGetAll("/sample/security/login", "/sample/security/signup", "/static/**")
            .permit(HttpMethod.POST, "/sample/security/signup")
            .requestMapper(requestMapper)
            .identifier(authIdentifier)
            .finisher(authFinisher)
            .end()
        ;
    }
}
