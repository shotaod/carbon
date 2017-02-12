package org.carbon.web.server.jetty;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.conf.WebConfig;
import org.carbon.web.exception.ServerStartupException;
import org.carbon.web.handler.DefaultChainFactory;
import org.carbon.web.server.EmbedServer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class JettyServerBridge implements EmbedServer {

    @Inject
    private DefaultChainFactory factory;
    @Inject
    private WebConfig config;

    private Server server;

    public JettyServerBridge() {
        server = new Server();
    }

    private void commonSetting(ContextHandler handler) {
        handler.setMaxFormContentSize(config.getMaxContentSize());
    }

    private ContextHandler dispatchHandler() {
        ContextHandler contextHandler = new ContextHandler("/");
        HandlerWrapper dispatchHandler = new HandlerWrapper() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                factory.superChain().startAsync(request, response);
                // Carbon handle Any Case, so we don't delegate jetty
                baseRequest.setHandled(true);
            }
        };
        contextHandler.setHandler(dispatchHandler);
        return contextHandler;
    }

    private ContextHandler resourceHandler(Class base) {
        ServletHolder resourceServletHolder = new ServletHolder(DefaultServlet.class);
        resourceServletHolder.setInitParameter("acceptRanges", "false");
        resourceServletHolder.setInitParameter("dirAllowed","false");
        resourceServletHolder.setInitParameter("redirectWelcome","false");
        String resourceBase = Optional.ofNullable(base.getClassLoader().getResource(config.getResourceDirectory()))
                .map(URL::toString)
                .orElseThrow(ServerStartupException::new);
        resourceServletHolder.setInitParameter("resourceBase", resourceBase);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/" + config.getResourceOutPath());
        servletContextHandler.addServlet(resourceServletHolder, "/");
        return servletContextHandler;
    }

    @Override
    public void run(Class base) throws Exception {

        // context(/{STATIC_PATH}) -> resource
        // context(/) -> dispatch
        ContextHandler resourceContext = resourceHandler(base);
        ContextHandler dispatchHandler = dispatchHandler();

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        ContextHandler[] handlers = Arrays.stream(new ContextHandler[]{ resourceContext, dispatchHandler})
                .peek(handler -> handler.setMaxFormContentSize(config.getMaxContentSize()))
                .toArray(ContextHandler[]::new);
        contexts.setHandlers(handlers);

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(config.getPort());
        connector.setRequestHeaderSize(config.getMaxHeaderSize());

        server.setHandler(contexts);
        server.setConnectors(new Connector[]{connector});
        server.start();
    }

    @Override
    public void await() throws Exception {
        server.join();
    }
}
