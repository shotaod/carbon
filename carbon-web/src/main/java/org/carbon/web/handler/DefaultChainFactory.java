package org.carbon.web.handler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.Describable;
import org.carbon.web.handler.scope.LoggingScopeChain;
import org.carbon.web.handler.scope.RequestScopeChain;
import org.carbon.web.handler.scope.SessionScopeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class DefaultChainFactory implements Describable {
    private Logger logger = LoggerFactory.getLogger(DefaultChainFactory.class);
    private Set<Class<? extends HandlerChain>> DefaultChains = Stream.of(
            ResponseTranslatorChain.class,
            LoggingScopeChain.class,
            SessionScopeChain.class,
            RequestScopeChain.class,
            CrossOriginChain.class,
            CoreDispatchChain.class
    ).collect(Collectors.toSet());

    @Assemble
    private ResponseTranslatorChain responseTranslatorChain;
    @Assemble
    private LoggingScopeChain loggingScopeChain;
    @Assemble
    private SessionScopeChain sessionScopeChain;
    @Assemble
    private RequestScopeChain requestScopeChain;
    @Assemble
    private CrossOriginChain crossOriginChain;
    @Assemble
    private CoreDispatchChain coreDispatchChain;

    @Assemble
    private List<HandlerChain> handlers;

    public HandlerChain factorize() {
        logger.debug("[chain] start initialize");
        List<HandlerChain> additionalHandlers = handlers.stream()
                .filter(handler -> DefaultChains.stream().noneMatch(defaultChain -> defaultChain.isAssignableFrom(handler.getClass())))
                .collect(Collectors.toList());

        if (logger.isDebugEnabled()) {
            if (additionalHandlers.isEmpty()) {
                logger.debug("No additional handler is found");
            } else
                additionalHandlers.forEach(handler -> logger.debug("Detect additional handler {}", handler.getClass()));
        }

        loggingScopeChain
                .withChain(sessionScopeChain)
                .withChain(requestScopeChain)
                .withChain(responseTranslatorChain)
                .withChain(crossOriginChain)
                .withChains(additionalHandlers)
                .withChain(coreDispatchChain);
        logger.debug("[chain] finish initialize");

        if (logger.isInfoEnabled()) {
            logger.info(describe());
        }
        return loggingScopeChain;
    }

    @Override
    public String describe() {
        return ChapterAttr.getBuilder("Chain Structure Result")
                .appendLine(loggingScopeChain.describe())
                .toString();
    }
}
