package org.carbon.web.handler;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Inject
    private ErrorWrapperChain errorWrapperChain;
    @Inject
    private CharacterEncodingChain encodingChain;
    @Inject
    private XHttpHeaderChain xHeaderChain;
    @Inject
    private LoggingScopeChain loggingScopeChain;
    @Inject
    private SessionScopeChain sessionScopeChain;
    @Inject
    private RequestScopeChain requestScopeChain;
    @Inject
    private CoreDispatchChain coreDispatchChain;

    public HttpHandlerChain factorize() {
        logger.debug("[chain] start initialize");
        encodingChain
                .setChain(loggingScopeChain)
                .setChain(xHeaderChain)
                .setChain(sessionScopeChain)
                .setChain(requestScopeChain)
                .setChain(errorWrapperChain)
                .setChain(coreDispatchChain);
        logger.debug("[chain] finish initialize");

        if (logger.isInfoEnabled()) {
            logger.info(resultInfo());
        }
        return encodingChain;
    }

    private String resultInfo() {
        String chainResult = Stream.of(
            encodingChain,
            loggingScopeChain,
            xHeaderChain,
            sessionScopeChain,
            requestScopeChain,
            errorWrapperChain,
            coreDispatchChain
        )
        .map(chain -> "└─ "+chain.getChainName())
        .collect(Collectors.joining("\n"));

        return ChapterAttr.getBuilder("Chain Processor Result")
                .appendLine(chainResult)
                .toString();
    }
}
