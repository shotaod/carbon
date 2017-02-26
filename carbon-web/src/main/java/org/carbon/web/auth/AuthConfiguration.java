package org.carbon.web.auth;

import java.util.List;
import java.util.Map;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;

/**
 * TODO add security logic per @Action
 *
 * @author Shota Oda 2016/10/28.
 */
@Configuration
public class AuthConfiguration {

    @Inject(optional = true)
    private AuthConfigAdapter configAdapter;

    @Component
    public AuthStrategyContext map() {
        AuthDefinition definition = new AuthDefinition();
        configAdapter.configure(definition);
        List<AuthStrategy<? extends AuthIdentity>> strategies = definition.getStrategies();
        return new AuthStrategyContext(strategies);
    }
}
