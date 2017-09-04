package org.carbon.web.server.jetty;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.handler.DefaultChainFactory;
import org.carbon.web.handler.HandlerChain;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * @author Shota Oda 2017/07/22.
 */
@Component
public class DispatchHandler extends ContextHandler {

    @Inject
    private DefaultChainFactory factory;
    private HandlerChain rootChain;

    @AfterInject
    public void afterInject() {
        rootChain = factory.factorize();
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        rootChain.startAsync(request, response);
        // Carbon handle Any Case, so we don't delegate jetty
        baseRequest.setHandled(true);
    }
}
