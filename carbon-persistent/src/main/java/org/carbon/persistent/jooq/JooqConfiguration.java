package org.carbon.persistent.jooq;

import java.util.List;
import javax.sql.DataSource;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.annotation.Transparent;
import org.carbon.persistent.DialectResolver;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DefaultDSLContext;

import static org.carbon.persistent.ConnectionTester.testConnection;

/**
 * @author Shota Oda 2016/11/26.
 */
@Transparent /* managed by PersistentModuleConfigurer */
@Configuration
public class JooqConfiguration {
    @Inject
    private DataSource dataSource;
    @Assemble
    private List<DAOImpl> daoImpls;

    @Component
    public DSLContext dslContext() {
        testConnection(dataSource);
        DialectResolver.Dialect dialect = DialectResolver.resolve();
        DSLContext context;
        switch (dialect) {
            case MySql:
                context = new DefaultDSLContext(dataSource, SQLDialect.MYSQL);
                break;
            case Postgres:
                context = new DefaultDSLContext(dataSource, SQLDialect.POSTGRES);
                break;
            default:
                throw new IllegalStateException("Cannot resolve Sql dialect");
        }

        // side effect
        if (daoImpls != null) {
            for (DAOImpl daoImpl : daoImpls) {
                daoImpl.setConfiguration(context.configuration());
            }
        }

        return context;
    }
}
