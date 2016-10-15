package org.dabuntu.web;

import org.dabuntu.component.ComponentFaced;
import org.dabuntu.web.context.InstanceContainer;
import org.dabuntu.web.context.MappedActionContainer;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.core.ActionMapper;
import org.dabuntu.web.def.FactoryAcceptAnnotations;
import org.dabuntu.web.def.Tomato;
import org.dabuntu.web.handler.CoreHandler;
import org.dabuntu.web.handler.ErrorWrapper;
import org.dabuntu.web.handler.RequestLoggingHandler;
import org.dabuntu.web.handler.RequestScopeWrapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	private Class confBase = ConfigurationBase.class;
	private int port = 8080;

	// TODO configuration
	private ComponentFaced componentFaced = new ComponentFaced();
	private ActionMapper actionMapper = new ActionMapper();

	// ===================================================================================
	//                                                                          Setter
	//                                                                          ==========
	public void setPort(int port) {
		this.port = port;
	}

	// ===================================================================================
	//                                                                              Public
	//                                                                              ======
	public void start(Class scanBase) throws Exception{
		logger.info(new Tomato().tomato);

		// resolve configurations and webInstances
		setupPool(scanBase);

		// get Dispatcher
		InstanceContainer appInstancePool = ApplicationPool.instance.getAppPool();
		CoreHandler dispatcher = appInstancePool.getInstanceByType(CoreHandler.class);

		runApplication(dispatcher);
	}

	// ===================================================================================
	//                                                                             Private
	//                                                                             =======
	private void setupPool(Class scanBase) throws Exception{
		// load Configuration -> create configurations
		Set<Class> frameworkManaged = componentFaced.scan(confBase, FactoryAcceptAnnotations.basic());
		Set<Class> clientManaged = componentFaced.scan(scanBase, FactoryAcceptAnnotations.basic());
		Set<Class> allManaged = Stream.concat(frameworkManaged.stream(), clientManaged.stream()).collect(Collectors.toSet());
		Map<Class, Object> webInstances = componentFaced.generate(allManaged);

		// mapping request to action
		MappedActionContainer mappedActionPool = actionMapper.map(webInstances.keySet().stream().collect(Collectors.toList()));

		// set up app pool
		ApplicationPool.instance.setPool(webInstances);
		ApplicationPool.instance.setPool(mappedActionPool);
	}

	private void runApplication(HandlerWrapper dispatcher) throws Exception{
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

		RequestScopeWrapper requestScopeWrapper = new RequestScopeWrapper();

		// context -> session -> logging -> error -> request scope -> dispatcher
		baseContext.setHandler(sessionHandler);
		sessionHandler.setHandler(logHandler);
		logHandler.setHandler(errorWrapper);
		errorWrapper.setHandler(requestScopeWrapper);
		requestScopeWrapper.setHandler(dispatcher);

		server.setHandler(baseContext);

		server.start();
		server.join();
	}
}
