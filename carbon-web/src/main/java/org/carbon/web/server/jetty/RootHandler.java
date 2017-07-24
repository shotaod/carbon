package org.carbon.web.server.jetty;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;

/**
 * @author garden 2017/07/22.
 */
@Component
public class RootHandler extends HandlerWrapper {

    private class SwitchHandler extends HandlerCollection {
        public SwitchHandler() {
            super();
            setHandlers(new Handler[] {staticHandler, dispatchHandler});
        }

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            if (staticHandler.canHandle(request)) {
                staticHandler.handle(target, baseRequest, request, response);
            } else {
                dispatchHandler.handle(target, baseRequest, request, response);
            }
        }
    }
    @Inject
    private DispatchHandler dispatchHandler;
    @Inject
    private StaticHandler staticHandler;

    @AfterInject
    public void afterInject() {
        this._handler = new SwitchHandler();
    }
}
