package org.carbon.sample.v2.conf.auth;

import org.carbon.authentication.conf.AuthConfigAdapter;
import org.carbon.authentication.conf.AuthDefinitionBuilder;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.v2.conf.auth.identity.SampleV2AuthIdentifier;
import org.carbon.sample.v2.conf.auth.identity.SampleV2AuthIdentity;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/12.
 */
@Configuration
public class SecurityConfiguration implements AuthConfigAdapter {
    @Inject
    private SampleV2AuthIdentifier authIdentifier;

    @Override
    public void configure(AuthDefinitionBuilder config) {
        config
                .defineForPage(SampleV2AuthIdentity.class)
                    .base("/user", "/task")
                    .authTo(HttpMethod.POST, "/user/login")
                    .logout("/user/logout")
                    .redirect("/user/login")
                    .permitGetAll("/user/login", "/user/signup", "/task/about", "/static/**")
                    .permit(HttpMethod.POST, "/user/signup")
                    .requestKey("email", "password")
                    .identifier(authIdentifier)
                .end()
                .defineForAPI(SampleV2AuthIdentity.class)
                    .base("/api/v1/rocketty")
                    .authTo(HttpMethod.POST, "/api/v1/rocketty/auth")
                    .end();
    }
}
