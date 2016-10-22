package org.dabuntu.web.server.jetty;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.handler.DefaultChainFactory;
import org.dabuntu.web.server.EmbedServer;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class JettyServerBridge implements EmbedServer {

	@Inject
	private DefaultChainFactory factory;
	private Server _server = this.server();

	private Server server() {
		Server server = new Server(8080);

		server.setHandler(this.handlerWrapper());

		return server;
	}

	private HandlerWrapper handlerWrapper() {
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		HandlerWrapper handler = new HandlerWrapper() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				factory.parentChain().startAsync(request, response);
				// Dabunt handle Any Case, so we don't delegate jetty
				baseRequest.setHandled(true);
			}
		};
		context.setHandler(handler);

		return context;
	}

	@Override
	public void run() throws Exception {
		this._server.start();
	}

	@Override
	public void await() throws Exception {
		this._server.join();
	}
}
