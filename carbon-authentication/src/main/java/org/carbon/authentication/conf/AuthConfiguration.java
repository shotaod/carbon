package org.carbon.authentication.conf;

import org.carbon.authentication.strategy.AuthStrategyRepository;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.web.context.session.SessionPool;

/**
 * TODO add security logic per @Action
 *
 * @author Shota Oda 2016/10/28.
 */
@Configuration
public class AuthConfiguration {

    @Assemble
    private AuthConfigAdapter configAdapter;
    @Assemble
    private SessionPool sessionContext;
    @Assemble
    private AuthDefinitionBuilder authDefinitionBuilder;

    @Component
    public AuthStrategyRepository authStrategyRepository() {
        configAdapter.configure(authDefinitionBuilder);
        return authDefinitionBuilder.buildRepository(sessionContext);
    }
}
