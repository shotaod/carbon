package org.carbon.web.server.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.handler.DefaultChainFactory;
import org.carbon.web.server.EmbedServer;

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

	private Tomcat _server = tomcat();
	@Inject
	private DefaultChainFactory chainFactory;

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
		chainFactory.superChain().startAsync(request, response);
	}

	private Tomcat tomcat() {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		// all programmatic, no server.xml or web.xml used
		Context context = tomcat.addContext("", null);
		Tomcat.addServlet(context, "core", httpServlet());
		context.addServletMappingDecoded("/*", "core");

		return tomcat;
	}

	@Override
	public void run(Class base) throws Exception {
		_server.start();
	}

	@Override
	public void await() throws Exception {
		_server.getServer().await();
	}
}
