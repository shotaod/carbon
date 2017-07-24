package org.carbon.web.handler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.util.format.ChapterAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class DefaultChainFactory {
    private Logger logger = LoggerFactory.getLogger(DefaultChainFactory.class);
    private Set<Class<? extends HandlerChain>> DefaultChains = Stream.of(
            CharacterEncodingChain.class,
            ErrorWrapperChain.class,
            LoggingScopeChain.class,
            SessionScopeChain.class,
            RequestScopeChain.class,
            CrossOriginChain.class,
            CoreDispatchChain.class,
            XHttpHeaderChain.class
    ).collect(Collectors.toSet());

    @Inject
    private CharacterEncodingChain encodingChain;
    @Inject
    private ErrorWrapperChain errorWrapperChain;
    @Inject
    private LoggingScopeChain loggingScopeChain;
    @Inject
    private SessionScopeChain sessionScopeChain;
    @Inject
    private RequestScopeChain requestScopeChain;
    @Inject
    private CrossOriginChain crossOriginChain;
    @Inject
    private CoreDispatchChain coreDispatchChain;
    @Inject
    private XHttpHeaderChain xHeaderChain;

    @Assemble
    private List<HandlerChain> handlers;

    public HandlerChain factorize() {
        logger.debug("[chain] start initialize");
        List<HandlerChain> additionalHandlers = handlers.stream()
                .filter(handler -> !DefaultChains.stream().anyMatch(defaultChain -> defaultChain.isAssignableFrom(handler.getClass())))
                .collect(Collectors.toList());

        if (logger.isDebugEnabled()) {
            additionalHandlers.forEach(handler -> logger.debug("Detect additional handler {}", handler.getClass()));
            if (additionalHandlers.isEmpty()) {
                logger.debug("No additional handler is found");
            }
        }

        encodingChain
                .withChain(loggingScopeChain)
                .withChain(sessionScopeChain)
                .withChain(requestScopeChain)
                .withChain(errorWrapperChain)
                .withChain(crossOriginChain)
                .setChains(additionalHandlers)
                .withChain(coreDispatchChain)
                .withChain(xHeaderChain);
        logger.debug("[chain] finish initialize");

        if (logger.isInfoEnabled()) {
            logger.info(resultInfo());
        }
        return encodingChain;
    }

    private String resultInfo() {

        return ChapterAttr.getBuilder("Chain Processor Result")
                .appendLine(encodingChain.getChainResult())
                .toString();
    }
}
