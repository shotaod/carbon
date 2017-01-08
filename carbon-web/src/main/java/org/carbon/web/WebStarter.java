package org.carbon.web;

import org.carbon.component.ComponentFaced;
import org.carbon.component.generator.CallbackConfiguration;
import org.carbon.persistent.DataSourceConfig;
import org.carbon.persistent.HibernateConfigurer;
import org.carbon.persistent.JooqConfigurer;
import org.carbon.persistent.PersistentImplementation;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.persistent.proxy.TransactionInterceptor;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.conf.ConfigHolder;
import org.carbon.web.conf.WebConfig;
import org.carbon.web.context.ApplicationPool;
import org.carbon.web.context.InstanceContainer;
import org.carbon.web.context.ActionDefinitionContainer;
import org.carbon.web.context.RequestContainer;
import org.carbon.web.context.SecurityContainer;
import org.carbon.web.core.SecurityConfigurator;
import org.carbon.web.core.mapping.ActionMapper;
import org.carbon.web.def.FactoryAcceptAnnotations;
import org.carbon.web.def.Logo;
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
			prepare(scanBase);
		} catch (Exception e) {
			logger.error("Application Start-Up Error", e);
		}
	}

	private void prepare(Class scanBase) throws Exception{
		logger.info(new Logo().logo);
		logger.info(ChapterAttr.get("Carbon Initialize Started"));

        Map<Class, Object> dependency = new HashMap<>();

        ConfigHolder configHolder = new ConfigHolder("config.yml");
        dependency.put(ConfigHolder.class, configHolder);

        dependency.putAll(setupPersistence(scanBase, configHolder));

        WebConfig webConfig = configHolder.findOne("web", WebConfig.class);
        dependency.put(WebConfig.class, webConfig);

        resolveDependency(scanBase, dependency);
        setupWeb();
        setupSecurity();

		// get Server
		InstanceContainer appInstancePool = ApplicationPool.instance.getAppPool();
		EmbedServer embedServer = appInstancePool.getByType(JettyServerBridge.class);
		embedServer.run(scanBase);

		logger.info(ChapterAttr.get("Carbon Initialize Finished"));

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
				.map(DataSourceConfig::toMysqlDataSource)
				.collect(Collectors.toList());
		dependency.put(DataSource.class, dataSources.get(0));
		switch (persistentImplementation) {
			case Hibernate:
				Set<Class<?>> entities = componentFaced.scan(scanBase, Collections.singleton(Entity.class));
				List<String> entityFqns = entities.stream().map(Class::getName).collect(Collectors.toList());

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

	private void resolveDependency(Class scanBase, Map<Class, Object> dependency) throws Exception{
		ComponentFaced componentFaced = new ComponentFaced();

		// load component -> create component
		Set<Class<?>> frameworkManaged = componentFaced.scan(ConfigurationBase.class, FactoryAcceptAnnotations.basic);
		Set<Class<?>> clientManaged = componentFaced.scan(scanBase, FactoryAcceptAnnotations.basic);
		Set<Class<?>> allManaged = Stream.concat(frameworkManaged.stream(), clientManaged.stream()).collect(Collectors.toSet());

        Map<Class, Object> instances = componentFaced.generate(allManaged, dependency, setupCallbackConfiguration());
        ApplicationPool.instance.setPool(instances);
    }

	private void setupWeb() {
        ApplicationPool app = ApplicationPool.instance;
        InstanceContainer appInstances = app.getAppPool();
        ActionMapper actionMapper = appInstances.getByType(ActionMapper.class);
        ActionDefinitionContainer actions = actionMapper.map(appInstances.getInstances().values().stream().collect(Collectors.toList()));
        app.setPool(actions);
    }

    private void setupSecurity() {
        InstanceContainer appInstances = ApplicationPool.instance.getAppPool();
        SecurityConfigurator securityConfigurator = appInstances.getByType(SecurityConfigurator.class);
        SecurityContainer securities = securityConfigurator.map(appInstances.getInstances());

        ApplicationPool.instance.setPool(securities);
    }

	private CallbackConfiguration setupCallbackConfiguration() {
		CallbackConfiguration callbackConfiguration = new CallbackConfiguration();

		// transaction
		callbackConfiguration.setCallbacks(TransactionInterceptor.class, clazz ->
            Arrays.stream(clazz.getDeclaredMethods()).anyMatch(method ->
                    method.isAnnotationPresent(Transactional.class)));

		return callbackConfiguration;
	}
}
