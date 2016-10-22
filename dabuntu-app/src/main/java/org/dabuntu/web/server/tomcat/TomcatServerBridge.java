package org.dabuntu.web.server.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.handler.HttpHandlerChain;
import org.dabuntu.web.server.EmbedServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class TomcatServerBridge implements EmbedServer{

	private Tomcat _server = this.tomcat();
	private HttpHandlerChain chain;

	private HttpServlet httpServlet() {
		return new HttpServlet() {
			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				doHandle(req, resp);
			}

			@Override
			protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				doHandle(req, resp);
			}

			@Override
			protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				doHandle(req, resp);
			}

			@Override
			protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				doHandle(req, resp);
			}

			@Override
			protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				doHandle(req, resp);
			}

			@Override
			protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				doHandle(req, resp);
			}

			@Override
			protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				doHandle(req, resp);
			}
		};
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response) {
		chain.startSync(request, response);
	}

	private Tomcat tomcat() {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		Context context = tomcat.addContext("", ".");
		Tomcat.addServlet(context, "Core", this.httpServlet());
		context.addServletMappingDecoded("/*", "Core");

		return tomcat;
	}

	@Override
	public void run() throws Exception {
		_server.start();
	}

	@Override
	public void await() throws Exception {
		_server.getServer().await();
	}
}
