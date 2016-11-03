package org.dabuntu.web.handler;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class DefaultChainFactory {

	@Inject
	private ErrorWrapperChain errorWrapperChain;
	@Inject
	private CharacterEncodingChain encodingChain;
	@Inject
	private LoggingChain loggingChain;
	@Inject
	private SessionScopeChain sessionScopeChain;
	@Inject
	private RequestScopeChain requestScopeChain;
	@Inject
	private CoreDispatchChain coreDispatchChain;

	public HttpHandlerChain parentChain() {
		errorWrapperChain
				.setChain(encodingChain)
				.setChain(loggingChain)
				.setChain(sessionScopeChain)
				.setChain(requestScopeChain)
				.setChain(coreDispatchChain);
		return errorWrapperChain;
	}
}
