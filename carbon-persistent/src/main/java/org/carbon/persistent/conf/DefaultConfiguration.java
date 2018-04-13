package org.carbon.persistent.conf;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.modular.env.EnvironmentMapper;
import org.carbon.persistent.dialect.ClasspathDialectResolver;
import org.carbon.persistent.exception.DialectResolveException;
import org.carbon.persistent.prop.DataSourceProperty;
import org.carbon.persistent.vendor.PersistentImplementation;
import org.carbon.persistent.vendor.hibernate.HibernateConfiguration;
import org.carbon.persistent.vendor.jooq.JooqConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2018/01/06.
 */
@Configuration
public class DefaultConfiguration {
    private Logger logger = LoggerFactory.getLogger(DefaultConfiguration.class);

    private static final String ImplKey = "persistent.implementation";

    @Assemble
    private EnvironmentMapper environmentMapper;
    @Assemble(optional = true)
    private DataSourceProperty property;

    @Component
    public DataSource dataSource() throws DialectResolveException {
        logger.debug("configure datasource");
        if (property == null) {
            logger.info("Cannot Detect Datasource property. Expect to be loaded custom Datasource.");
            return null;
        }
        logger.info("Detect Datasource property. Create Pool Datasource {}", org.apache.tomcat.jdbc.pool.DataSource.class.getName());
        String dialect = property.getDialect();
        String host = property.getHost();
        Integer port = property.getPort();
        String db = property.getDb();
        String user = property.getUser();
        String password = property.getPassword();
        Map<String, String> params = property.getParameters();
        String dbUrl = String.format("jdbc:%s://%s:%s/%s", dialect, host, port, db);
        if (params != null) {
            String query = params.entrySet().stream()
                    .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                    .collect(Collectors.joining("&", "?", ""));
            dbUrl += query;
        }

        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(ClasspathDialectResolver.resolve().getDriverClassName());
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        //dataSource.setDefaultAutoCommit(false);

        // configure pool
        DataSourceProperty.Pool pool = property.getPool();
        if (pool != null) {
            Optional.ofNullable(pool.getMaxActive())
                    .ifPresent(dataSource::setMaxActive);
            Optional.ofNullable(pool.getMaxIdle())
                    .ifPresent(dataSource::setMaxIdle);
            Optional.ofNullable(pool.getMinIdle())
                    .ifPresent(dataSource::setMaxIdle);
            Optional.ofNullable(pool.getInitialSize())
                    .ifPresent(dataSource::setInitialSize);
            Optional.ofNullable(pool.getMaxWait())
                    .ifPresent(dataSource::setMaxWait);
            Optional.ofNullable(pool.getTestOnBorrow())
                    .ifPresent(dataSource::setTestOnBorrow);
            Optional.ofNullable(pool.getTestOnConnect())
                    .ifPresent(dataSource::setTestOnConnect);
            Optional.ofNullable(pool.getTestOnReturn())
                    .ifPresent(dataSource::setTestOnReturn);
            Optional.ofNullable(pool.getTestWhileIdle())
                    .ifPresent(dataSource::setTestWhileIdle);
        }
        return dataSource;
    }

    @Component
    public PersistentImplSwitcher persistentSwitcher() {
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
