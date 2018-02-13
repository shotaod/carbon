package org.carbon.web.server.jetty;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.server.EmbedServer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class JettyServerBridge implements EmbedServer {
    @Inject
    private WebProperty config;
    @Inject
    private RootHandler rootHandler;

    private Server server;

    public JettyServerBridge() {
        server = new Server();
    }

    @Override
    public void run() throws Exception {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(config.getPort());
        Connector[] cons = {connector};
        server.setConnectors(cons);

        server.setHandler(rootHandler);
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", config.getMaxContentSize());
        // todo add header size
        // server.setAttribute("", config.getMaxHeaderSize());

        server.start();
    }

    @Override
    public void await() throws Exception {
        server.join();
    }
}
