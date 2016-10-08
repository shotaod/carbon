package org.dabuntu.web;

import org.dabuntu.component.ComponentManager;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.def.FactoryAcceptAnnotations;
import org.dabuntu.web.def.Tomato;
import org.dabuntu.web.handler.DefaultHandler;
import org.dabuntu.web.core.ActionMapper;
import org.dabuntu.web.container.MappedActionContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		server.setHandler(handler);
		server.start();
		server.join();
	}
}
