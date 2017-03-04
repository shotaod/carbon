package org.carbon.authentication;

import java.util.List;
import java.util.stream.Collectors;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.util.format.ChapterAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO add security logic per @Action
 *
 * @author Shota Oda 2016/10/28.
 */
@Configuration
public class AuthConfiguration {
    private static Logger logger = LoggerFactory.getLogger(AuthConfiguration.class);

    @Inject
    private AuthConfigAdapter configAdapter;

    @Component
    public AuthStrategyContext map() {
        AuthDefinition definition = new AuthDefinition();
        configAdapter.configure(definition);
        List<AuthStrategy<? extends AuthIdentity>> strategies = definition.getStrategies();
        if (logger.isInfoEnabled()) {
            String resultInfo = ChapterAttr.getBuilder("Authentication Configuration Result")
                    .appendLine(strategies
                            .stream().map(AuthStrategy::toString)
                            .collect(Collectors.joining("\n")
                            )
                    ).toString();
            logger.info(resultInfo);
        }
        return new AuthStrategyContext(strategies);
    }
}
