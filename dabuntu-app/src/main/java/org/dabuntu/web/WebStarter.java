package org.dabuntu.web;

import org.dabuntu.component.ComponentManager;
import org.dabuntu.web.context.MappedActionContainer;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.core.ActionMapper;
import org.dabuntu.web.def.FactoryAcceptAnnotations;
import org.dabuntu.web.def.Tomato;
import org.dabuntu.web.handler.DefaultHandler;
import org.dabuntu.web.handler.ErrorWrapper;
import org.dabuntu.web.handler.RequestLoggingHandler;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ubuntu 2016/10/06.
 */
public class WebStarter {

	// ===================================================================================
	//                                                                          Logger
	//                                                                          ==========
	private Logger logger = LoggerFactory.getLogger(WebStarter.class);

	// ===================================================================================
	//                                                                            Settings
	//                                                                            ========
	private int port = 8080;
	private Handler handler = new DefaultHandler();
	private ComponentManager componentManager = new ComponentManager();
	private ActionMapper actionMapper = new ActionMapper();

	// ===================================================================================
	//                                                                          Setter
	//                                                                          ==========
	public void setPort(int port) {
		this.port = port;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	// ===================================================================================
	//                                                                              Public
	//                                                                              ======
	public void start(Class scanBase) throws Exception{
		logger.info(new Tomato().tomato);

		// initialize managed object
		Map<Class, Object> objectMap = componentManager.initialize(scanBase, FactoryAcceptAnnotations.get());

		// mapping request to action
		MappedActionContainer actionContainer = actionMapper.map(objectMap.keySet().stream().collect(Collectors.toList()));

		// set up app pool
		ApplicationPool.instance.setPool(objectMap);
		ApplicationPool.instance.setPool(actionContainer);

		runApplication();
	}

	// ===================================================================================
	//                                                                             Private
	//                                                                             =======
	private void runApplication() throws Exception{
		Server server = new Server(port);

		// base
		ContextHandler baseContext = new ContextHandler("/");

		// session manager
		HashSessionManager sessionManager = new HashSessionManager();
		sessionManager.setSessionCookie("DBTSESSIONID");
		sessionManager.setUsingCookies(true);

		// session handler
		SessionHandler sessionHandler = new SessionHandler(sessionManager);

		// Logging Handler
		RequestLoggingHandler logHandler = new RequestLoggingHandler();

		// Error Handler
		ErrorWrapper errorWrapper = new ErrorWrapper();

		// Dispatcher
		DefaultHandler defaultHandler = new DefaultHandler();

		// context -> session -> logging -> error -> dispatcher
		baseContext.setHandler(sessionHandler);
		sessionHandler.setHandler(logHandler);
		logHandler.setHandler(errorWrapper);
		errorWrapper.setHandler(defaultHandler);

		server.setHandler(baseContext);

		server.start();
		server.join();
	}

	// ===================================================================================
	//                                                                          Hot Deploy
	//                                                                          ==========
	private DeploymentManager deploymentManager() {
		DeploymentManager manager = new DeploymentManager();
		manager.setContexts(new ContextHandlerCollection());

		WebAppProvider webAppProvider = new WebAppProvider();
		webAppProvider.setMonitoredDirName(".");

		manager.setAppProviders(Collections.singleton(webAppProvider));

		return manager;
	}
}
