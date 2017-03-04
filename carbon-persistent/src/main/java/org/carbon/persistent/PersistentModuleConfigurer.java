package org.carbon.persistent;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;

import org.carbon.modular.ModuleConfigurationResult;
import org.carbon.modular.ModuleConfigurer;
import org.carbon.persistent.hibernate.AutoDDL;
import org.carbon.persistent.hibernate.HibernateConfiguration;
import org.carbon.persistent.jooq.JooqConfiguration;
import org.carbon.persistent.prop.DataSourceProperty;
import org.carbon.persistent.prop.PersistentImplementation;
import org.carbon.persistent.proxy.TransactionInterceptor;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.format.StringLineBuilder;
import org.carbon.util.mapper.ConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/02/09.
 */
public class PersistentModuleConfigurer implements ModuleConfigurer {
    private Logger logger = LoggerFactory.getLogger(PersistentModuleConfigurer.class);

    private final String ImplKey = "persistent.implementation";
    private final String DataSourceKey = "persistent.dataSource";
    private final String AutoDDLKey = "persistent.option.autoddl";

    @Override
    public ModuleConfigurationResult configure(Class scanBase, ConfigHolder configHolder) {
        PersistentImplementation persistentImplementation = getPersistentImplementation(configHolder);
        logger.debug("Persistent implementation is {}", persistentImplementation);

        // set up instances
        logger.debug("① Start set up instances managed by PersistentModule");
        Map<Class<?>, Object> instances = new HashMap<>();
        if (persistentImplementation == PersistentImplementation.Hibernate) {
            AutoDDL autoDDL = configHolder.findPrimitive(AutoDDLKey, String.class).map(AutoDDL::actionOf).orElse(AutoDDL.None);
            instances.put(AutoDDL.class, autoDDL);
        }

        Optional<DataSourceProperty> optionalProperty = configHolder.findOne(DataSourceKey, DataSourceProperty.class);
        if (optionalProperty.isPresent()) {
            logger.info("Detect Datasource property. Create and Inject Datasource");
            instances.put(DataSource.class, optionalProperty.get().toDataSource());
        } else {
            logger.info("Cannot Detect Datasource property. Expect to be loaded custom Datasource.");
        }

        // set up classes
        logger.debug("② Start set up classes by PersistentModule");
        Set<Class<?>> classes = new HashSet<>();
        switch (persistentImplementation) {
            case Hibernate:
                classes.add(HibernateConfiguration.class);
                break;
            case Jooq:
                classes.add(JooqConfiguration.class);
                break;
            // noop
            // case None:
            // default:
        }

        if (logger.isInfoEnabled()) {
            Stream<String> instancesStream = instances.keySet().stream().map(clazz -> "- " + clazz.getName());
            Stream<String> configStream = classes.stream().map(clazz -> "- " + clazz.getName());
            String dependencies = Stream.concat(instancesStream, configStream).collect(Collectors.joining("\n"));
            StringLineBuilder resultInfo = ChapterAttr.getBuilder("Persistent Configure Result").appendLine(dependencies);
            logger.info(resultInfo.toString());
        }
        return new ModuleConfigurationResult(classes, instances, Collections.singleton(PersistentScanBase.class));
    }

    private PersistentImplementation getPersistentImplementation(ConfigHolder configHolder) {
        return configHolder
            .findPrimitive(ImplKey, String.class)
            .map(PersistentImplementation::implOf)
            .orElse(PersistentImplementation.None);
    }
}
