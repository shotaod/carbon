package org.carbon.web.server.jetty;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.server.EmbedServer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

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
    public void run(Class base) throws Exception {

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(config.getPort());
        connector.setRequestHeaderSize(config.getMaxHeaderSize());
        connector.setRequestBufferSize(config.getMaxContentSize());

        server.setHandler(rootHandler);
        server.setConnectors(new Connector[]{connector});
        server.start();
    }

    @Override
    public void await() throws Exception {
        server.join();
    }
}
