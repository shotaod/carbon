package org.carbon.web.server.jetty;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.exception.ServerStartupException;
import org.carbon.web.handler.DefaultChainFactory;
import org.carbon.web.handler.HttpHandlerChain;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class JettyServerBridge implements EmbedServer {
    private Logger logger = LoggerFactory.getLogger(EmbedServer.class);
    @Inject
    private DefaultChainFactory factory;
    @Inject
    private WebProperty config;

    private Server server;

    public JettyServerBridge() {
        server = new Server();
    }

    private ContextHandler dispatchHandler() {
        ContextHandler contextHandler = new ContextHandler("/");
        HttpHandlerChain handlerChain = factory.factorize();
        HandlerWrapper dispatchHandler = new HandlerWrapper() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                handlerChain.startAsync(request, response);
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
        WebProperty.Resource resource = config.getResource();
        String resourceDirectory = resource.getDirectory();
        String resourceBase = Optional.ofNullable(base.getClassLoader().getResource(resourceDirectory))
            .map(URL::toString)
            .orElseThrow(() -> new ServerStartupException(resourceSetupFailMessage(resourceDirectory)));
        resourceServletHolder.setInitParameter("resourceBase", resourceBase);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/" + resource.getOutPath());
        servletContextHandler.addServlet(resourceServletHolder, "/");
        return servletContextHandler;
    }

    @Override
    public void run(Class base) throws Exception {

        // context(/{STATIC_PATH}) -> resource
        // context(/) -> dispatch
        List<ContextHandler> handlers = new ArrayList<>();
        handlers.add(dispatchHandler());
        WebProperty.Resource resource = config.getResource();
        if (resource == null || resource.getDirectory() == null || resource.getOutPath() == null) {
            logger.info("Not found resource setting, so skip Resource handler mapping");
        } else {
            handlers.add(resourceHandler(base));
        }

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        ContextHandler[] readyHandlers = handlers.stream()
                .peek(handler -> handler.setMaxFormContentSize(config.getMaxContentSize()))
                .toArray(ContextHandler[]::new);
        contexts.setHandlers(readyHandlers);

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

    private String resourceSetupFailMessage(String resourceDirectory) {
        return String.format("Fail to load Resource directory: %s\n--------------------------------------------------------------------------------\nTry either\n・ Delete resource setting property\nor\n・ Create dir: resource/%s and stuff some resource file\n--------------------------------------------------------------------------------", resourceDirectory, resourceDirectory);
    }
}
