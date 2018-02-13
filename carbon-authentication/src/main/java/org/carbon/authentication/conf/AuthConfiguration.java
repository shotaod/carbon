package org.carbon.authentication.conf;

import java.util.stream.Collectors;

import org.carbon.authentication.strategy.AuthStrategy;
import org.carbon.authentication.strategy.AuthStrategyRepository;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.context.session.SessionContext;
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
    @Inject
    private SessionContext sessionContext;

    @Component
    public AuthStrategyRepository authStrategyRepository() {
        AuthDefinitionBuilder definition = new AuthDefinitionBuilder();
        configAdapter.configure(definition);
        AuthStrategyRepository repo = definition.buildRepository(sessionContext);
        if (logger.isInfoEnabled()) {
            String resultInfo = ChapterAttr.getBuilder("Authentication Configuration Result")
                    .appendLine(repo.findAll()
                            .stream().map(AuthStrategy::toString)
                            .collect(Collectors.joining("\n")
                            )
                    ).toString();
            logger.info(resultInfo);
        }
        return repo;
    }
}
