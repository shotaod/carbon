package org.carbon.web;

import org.carbon.web.conf.ConfigHolder;
import org.carbon.web.conf.WebConfig;
import org.carbon.web.context.ApplicationPool;
import org.carbon.web.context.InstanceContainer;
import org.carbon.web.context.MappedActionContainer;
import org.carbon.web.context.SecurityContainer;
import org.carbon.web.core.ActionMapper;
import org.carbon.web.core.SecurityConfigurator;
import org.carbon.web.def.FactoryAcceptAnnotations;
import org.carbon.web.def.Logo;
import org.carbon.persistent.DataSourceConfig;
import org.carbon.persistent.HibernateConfigurer;
import org.carbon.persistent.JooqConfigurer;
import org.carbon.persistent.PersistentImplementation;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.persistent.proxy.TransactionInterceptor;
import org.carbon.component.ComponentFaced;
import org.carbon.component.generator.CallbackConfiguration;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.server.EmbedServer;
import org.carbon.web.server.jetty.JettyServerBridge;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2016/10/06.
 */
public class WebStarter {

	// ===================================================================================
	//                                                                          Logger
	//                                                                          ==========
	private Logger logger = LoggerFactory.getLogger(WebStarter.class);

	// ===================================================================================
	//                                                                              Public
	//                                                                              ======
	public void start(Class scanBase) {
		try {
			doStart(scanBase);
		} catch (Exception e) {
			logger.error("Application Start-Up Error", e);
		}
	}
	private void doStart(Class scanBase) throws Exception{
		logger.info(new Logo().logo);
		logger.info(ChapterAttr.get("Dabunt Initialize Started"));

		ConfigHolder configHolder = new ConfigHolder("config.yml");

		Map<Class, Object> dependency = setupPersistence(scanBase, configHolder);

        WebConfig webConfig = configHolder.find("web", WebConfig.class).get(0);
        dependency.put(WebConfig.class, webConfig);

        // Must Call! to resolve configurations and web-managed instances
		setupWebPool(scanBase, dependency);

		// get Server
		InstanceContainer appInstancePool = ApplicationPool.instance.getAppPool();
		EmbedServer embedServer = appInstancePool.getInstanceByType(JettyServerBridge.class);
		embedServer.run(scanBase);

		logger.info(ChapterAttr.get("Dabunt Initialize Finished"));

		embedServer.await();
	}

	// ===================================================================================
	//                                                                             Private
	//                                                                             =======
	private Map<Class, Object> setupPersistence(Class scanBase, ConfigHolder configHolder) throws Exception{
		ComponentFaced componentFaced = new ComponentFaced();

		PersistentImplementation persistentImplementation = configHolder.findPrimitive("persistent.implementation", String.class)
				.map(PersistentImplementation::nameOf)
				.orElse(PersistentImplementation.None);

		Map<Class, Object> dependency = new HashMap<>();

		List<DataSource> dataSources = configHolder
				.find("persistent.dataSources.source", DataSourceConfig.class).stream()
				.map(config -> config.toMysqlDataSource())
				.collect(Collectors.toList());
		dependency.put(DataSource.class, dataSources.get(0));
		switch (persistentImplementation) {
			case Hibernate:
				Set<Class> entities = componentFaced.scan(scanBase, Collections.singleton(Entity.class));
				List<String> entityFqns = entities.stream().map(entity -> entity.getName()).collect(Collectors.toList());

				String autoddl = configHolder.findPrimitive("persistent.autoddl", String.class).orElse("");

				EntityManagerFactory emf = new HibernateConfigurer("db1", dataSources.get(0), entityFqns)
						.setAutoddl(autoddl)
						.createEntityManagerFactory();

				dependency.put(EntityManagerFactory.class, emf);
				break;
			case Jooq:
				DSLContext dslContext = new JooqConfigurer(dataSources.get(0)).createDSLContext();
				dependency.put(DSLContext.class, dslContext);
				break;
			case None:
		}

		return dependency;
	}

	private void setupWebPool(Class scanBase, Map<Class, Object> dependency) throws Exception{
		ComponentFaced componentFaced = new ComponentFaced();
		ActionMapper actionMapper = new ActionMapper();
		SecurityConfigurator securityConfigurator = new SecurityConfigurator();

		// load component -> create component
		Set<Class> frameworkManaged = componentFaced.scan(ConfigurationBase.class, FactoryAcceptAnnotations.basic());
		Set<Class> clientManaged = componentFaced.scan(scanBase, FactoryAcceptAnnotations.basic());
		Set<Class> allManaged = Stream.concat(frameworkManaged.stream(), clientManaged.stream()).collect(Collectors.toSet());

		Map<Class, Object> webInstances = componentFaced.generate(allManaged, dependency, setupCallbackConfiguration());

		// mapping request path to action
		MappedActionContainer mappedActionPool = actionMapper.map(webInstances.keySet().stream().collect(Collectors.toList()));

		// configure security
		SecurityContainer securityPool = securityConfigurator.map(webInstances);

		// set up app pool
		ApplicationPool.instance.setPool(webInstances);
		ApplicationPool.instance.setPool(mappedActionPool);
		ApplicationPool.instance.setPool(securityPool);
	}

	private CallbackConfiguration setupCallbackConfiguration() {
		CallbackConfiguration callbackConfiguration = new CallbackConfiguration();

		// transaction
		callbackConfiguration.setCallbacks(TransactionInterceptor.class, clazz -> {
			return Arrays.stream(clazz.getDeclaredMethods())
				.anyMatch(method -> method.isAnnotationPresent(Transactional.class));
		});

		return callbackConfiguration;
	}
}
