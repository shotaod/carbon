package org.carbon.persistent.vendor.jooq;

import java.util.List;
import javax.sql.DataSource;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.annotation.Switch;
import org.carbon.persistent.conf.PersistentImplSwitcher;
import org.carbon.persistent.dialect.ClasspathDialectResolver;
import org.carbon.persistent.dialect.Dialect;
import org.carbon.persistent.exception.DialectResolveException;
import org.carbon.persistent.prop.PersistentOptionProperty;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DefaultDSLContext;

/**
 * @author Shota Oda 2016/11/26.
 */
@Switch(PersistentImplSwitcher.class)
@Configuration
public class JooqConfiguration {
    @Inject
    private DataSource dataSource;
    @Assemble
    private List<DAOImpl> daoImpls;

    @Component
    public DSLContext dslContext() throws DialectResolveException {
        Dialect dialect = ClasspathDialectResolver.resolve();
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
            daoImpls.forEach(impl -> impl.setConfiguration(context.configuration()));
        }

        return context;
    }
}
