package org.carbon.persistent.conf;

import javax.sql.DataSource;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.modular.env.EnvironmentMapper;
import org.carbon.persistent.hibernate.HibernateConfiguration;
import org.carbon.persistent.jooq.JooqConfiguration;
import org.carbon.persistent.prop.DataSourceProperty;
import org.carbon.persistent.prop.PersistentImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2018/01/06.
 */
@Configuration
public class DefaultConfiguration {
    private Logger logger = LoggerFactory.getLogger(DefaultConfiguration.class);

    private static final String DataSourceKey = "persistent.dataSource";
    private static final String ImplKey = "persistent.implementation";

    @Inject
    private EnvironmentMapper environmentMapper;

    @Component
    public DataSource dataSource() {
        logger.debug("configure datasource");
        return environmentMapper.mapOptional(DataSourceKey, DataSourceProperty.class)
                .map(property -> {
                    logger.info("Detect Datasource property. Create Datasource");
                    return property.toDataSource();
                })
                .orElseGet(() -> {
                    logger.info("Cannot Detect Datasource property. Expect to be loaded custom Datasource.");
                    return null;
                });
    }

    @Component
    public PersistentSwitcher persistentSwitcher() {
        return target -> {
            PersistentImplementation persistentImplementation = environmentMapper
                    .findPrimitive(ImplKey, String.class)
                    .map(PersistentImplementation::implOf)
                    .orElse(PersistentImplementation.None);
            logger.debug("Setup Persistent implementation {}", persistentImplementation);
            switch (persistentImplementation) {
                case Hibernate:
                    return HibernateConfiguration.class.equals(target);
                case Jooq:
                    return JooqConfiguration.class.equals(target);
                // NoOp
                case None:
                default:
                    logger.warn("Not specified persistent implementation.");
                    return false;
            }
        };
    }
}
