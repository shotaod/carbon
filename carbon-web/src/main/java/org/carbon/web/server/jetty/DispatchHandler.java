package org.carbon.web.server.jetty;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.web.handler.DefaultChainFactory;
import org.carbon.web.handler.HandlerChain;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2017/07/22.
 */
@Component
public class DispatchHandler extends ContextHandler {

    @Assemble
    private DefaultChainFactory factory;
    private HandlerChain rootChain;

    @AfterAssemble
    public void afterInject() {
        rootChain = factory.factorize();
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        rootChain.startAsync(request, response);
        // Carbon handle Any Case, so we don't delegate jetty
        baseRequest.setHandled(true);
    }
}
