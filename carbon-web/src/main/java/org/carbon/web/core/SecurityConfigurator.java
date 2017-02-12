package org.carbon.web.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.carbon.component.annotation.Component;
import org.carbon.web.auth.AuthIdentity;
import org.carbon.web.auth.AuthStrategy;
import org.carbon.web.auth.SecurityConfigAdapter;
import org.carbon.web.auth.SecurityConfiguration;
import org.carbon.web.context.SecurityContainer;

/**
 * TODO add security logic per @Action
 * @author Shota Oda 2016/10/28.
 */
@Component
public class SecurityConfigurator {

    private class AuthAndUrl {
        private AuthStrategy strategy;
        private String url;
        public AuthAndUrl(AuthStrategy strategy, String url) {
            this.strategy = strategy;
            this.url = url;
        }
    }

    public SecurityContainer map(Map<Class, Object> context) {
        return findAdapter(context).map(adapter -> {
            SecurityConfiguration config = new SecurityConfiguration();
            adapter.configure(config);
            List<AuthStrategy<? extends AuthIdentity>> strategies = config.getStrategies();
            return new SecurityContainer(strategies);
        }).orElse(new SecurityContainer());
    }

    private Optional<SecurityConfigAdapter> findAdapter(Map<Class, Object> context) {
        return context.entrySet().stream()
            .filter(entry -> SecurityConfigAdapter.class.isAssignableFrom(entry.getKey()))
            .map(entry -> (SecurityConfigAdapter)entry.getValue())
            .findFirst();
    }
}
