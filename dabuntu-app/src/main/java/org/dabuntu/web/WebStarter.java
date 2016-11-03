package org.dabuntu.web;

import org.dabuntu.component.ComponentFaced;
import org.dabuntu.util.format.ChapterAttr;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.context.SecurityContainer;
import org.dabuntu.web.context.InstanceContainer;
import org.dabuntu.web.context.MappedActionContainer;
import org.dabuntu.web.core.ActionMapper;
import org.dabuntu.web.core.SecurityConfigurator;
import org.dabuntu.web.def.FactoryAcceptAnnotations;
import org.dabuntu.web.def.Tomato;
import org.dabuntu.web.server.EmbedServer;
import org.dabuntu.web.server.jetty.JettyServerBridge;
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
	//                                                                              Public
	//                                                                              ======
	public void start(Class scanBase) throws Exception{
		logger.info(new Tomato().tomato);
		logger.info(ChapterAttr.get("Dabunt Initialize Started"));

		// Must Call! to resolve configurations and webInstances
		this.setupPool(scanBase);

		// get Server
		InstanceContainer appInstancePool = ApplicationPool.instance.getAppPool();
		EmbedServer embedServer = appInstancePool.getInstanceByType(JettyServerBridge.class);
		embedServer.run();

		logger.info(ChapterAttr.get("Dabunt Initialize Finished"));

		embedServer.await();
	}

	// ===================================================================================
	//                                                                             Private
	//                                                                             =======
	private void setupPool(Class scanBase) throws Exception{
		ComponentFaced componentFaced = new ComponentFaced();
		ActionMapper actionMapper = new ActionMapper();
		SecurityConfigurator securityConfigurator = new SecurityConfigurator();

		// load Configuration -> create configurations
		Set<Class> frameworkManaged = componentFaced.scan(ConfigurationBase.class, FactoryAcceptAnnotations.basic());
		Set<Class> clientManaged = componentFaced.scan(scanBase, FactoryAcceptAnnotations.basic());
		Set<Class> allManaged = Stream.concat(frameworkManaged.stream(), clientManaged.stream()).collect(Collectors.toSet());
		Map<Class, Object> webInstances = componentFaced.generate(allManaged);

		// mapping request to action
		MappedActionContainer mappedActionPool = actionMapper.map(webInstances.keySet().stream().collect(Collectors.toList()));

		// configure security
		SecurityContainer securityPool = securityConfigurator.map(webInstances);

		// set up app pool
		ApplicationPool.instance.setPool(webInstances);
		ApplicationPool.instance.setPool(mappedActionPool);
		ApplicationPool.instance.setPool(securityPool);
	}
}
