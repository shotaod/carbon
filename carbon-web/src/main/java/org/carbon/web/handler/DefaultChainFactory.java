package org.carbon.web.handler;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class DefaultChainFactory {

    @Inject
    private ErrorWrapperChain errorWrapperChain;
    @Inject
    private CharacterEncodingChain encodingChain;
    @Inject
    private LoggingScopeChain loggingScopeChain;
    @Inject
    private SessionScopeChain sessionScopeChain;
    @Inject
    private RequestScopeChain requestScopeChain;
    @Inject
    private CoreDispatchChain coreDispatchChain;

    public HttpHandlerChain superChain() {
        errorWrapperChain
                .setChain(encodingChain)
                .setChain(loggingScopeChain)
                .setChain(sessionScopeChain)
                .setChain(requestScopeChain)
                .setChain(coreDispatchChain);
        return errorWrapperChain;
    }
}
