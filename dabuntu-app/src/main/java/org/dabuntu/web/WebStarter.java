package org.dabuntu.web;

import org.dabunt.persistent.DataSourceConfig;
import org.dabunt.persistent.PersistentFaced;
import org.dabuntu.component.ComponentFaced;
import org.dabuntu.util.format.ChapterAttr;
import org.dabuntu.web.conf.ConfigHolder;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.context.InstanceContainer;
import org.dabuntu.web.context.MappedActionContainer;
import org.dabuntu.web.context.SecurityContainer;
import org.dabuntu.web.core.ActionMapper;
import org.dabuntu.web.core.SecurityConfigurator;
import org.dabuntu.web.def.FactoryAcceptAnnotations;
import org.dabuntu.web.def.Logo;
import org.dabuntu.web.server.EmbedServer;
import org.dabuntu.web.server.jetty.JettyServerBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
		logger.info(new Logo().logo);
		logger.info(ChapterAttr.get("Dabunt Initialize Started"));

		ConfigHolder configHolder = new ConfigHolder("config.yml");

		Map<Class, Object> dependency = setupDatabase(scanBase, configHolder);

		// Must Call! to resolve configurations and web-managed instances
		scan(scanBase, dependency);

		// get Server
		InstanceContainer appInstancePool = ApplicationPool.instance.getAppPool();
		EmbedServer embedServer = appInstancePool.getInstanceByType(JettyServerBridge.class);
		embedServer.run();

		logger.info(ChapterAttr.get("Dabunt Initialize Finished"));

		embedServer.await();
	}

	private Map<Class, Object> setupDatabase(Class scanBase, ConfigHolder configHolder) throws Exception{
		ComponentFaced componentFaced = new ComponentFaced();
		Set<Class> entities = componentFaced.scan(scanBase, Collections.singleton(Entity.class));
		List<String> entityFqns = entities.stream().map(entity -> entity.getName()).collect(Collectors.toList());

		List<DataSourceConfig> dataSourceConfigs = configHolder.find("persistent.dataSource.source", DataSourceConfig.class);
		String autoddl = configHolder.findPrimitive("persistent.autoddl", String.class).orElse("");

		EntityManagerFactory emf = new PersistentFaced("db1", dataSourceConfigs.get(0), entityFqns)
				.setAutoddl(autoddl)
				.createEntityManagerFactory();

		Map<Class, Object> dependency = new HashMap<>();
		dependency.put(EntityManagerFactory.class, emf);

		return dependency;

	}

	// ===================================================================================
	//                                                                             Private
	//                                                                             =======
	private void scan(Class scanBase, Map<Class, Object> dependency) throws Exception{
		ComponentFaced componentFaced = new ComponentFaced();
		ActionMapper actionMapper = new ActionMapper();
		SecurityConfigurator securityConfigurator = new SecurityConfigurator();

		// load Configuration -> create configurations
		Set<Class> frameworkManaged = componentFaced.scan(ConfigurationBase.class, FactoryAcceptAnnotations.basic());
		Set<Class> clientManaged = componentFaced.scan(scanBase, FactoryAcceptAnnotations.basic());
		Set<Class> allManaged = Stream.concat(frameworkManaged.stream(), clientManaged.stream()).collect(Collectors.toSet());
		Map<Class, Object> webInstances = componentFaced.generate(allManaged, dependency);

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
