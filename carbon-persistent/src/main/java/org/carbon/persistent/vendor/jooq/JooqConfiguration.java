package org.carbon.persistent.vendor.jooq;

import java.util.List;
import javax.sql.DataSource;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Switch;
import org.carbon.persistent.conf.PersistentImplSwitcher;
import org.carbon.persistent.dialect.ClasspathDialectResolver;
import org.carbon.persistent.exception.DialectResolveException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DAOImpl;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;

/**
 * @author Shota Oda 2016/11/26.
 */
@Switch(PersistentImplSwitcher.class)
@Configuration
public class JooqConfiguration {
    @Assemble
    private JooqTransactionalConnectionProvider jooqConnectionProvider;
    @Assemble
    private DataSource dataSource;
    @Assemble
    private List<DAOImpl> daoImpls;

    @AfterAssemble
    public void afterAssemble() throws DialectResolveException {
        DefaultDSLContext noTransactionalContext = new DefaultDSLContext(dataSource, resolveJooQDialect());
        // side effect
        if (daoImpls != null) {
            daoImpls.forEach(impl -> impl.setConfiguration(noTransactionalContext.configuration()));
        }
    }

    @Component
    public DSLContext dslContext() throws DialectResolveException {
        return new DefaultDSLContext(new DefaultConfiguration()
                // connection provider
                .set(jooqConnectionProvider)
                // assume one dialect
                .set(resolveJooQDialect()));
    }

    private SQLDialect resolveJooQDialect() throws DialectResolveException {
        switch (ClasspathDialectResolver.resolve()) {
            case MySql:
                return SQLDialect.MYSQL;
            case Postgres:
                return SQLDialect.POSTGRES;
            default:
                throw new IllegalStateException("Cannot resolve Sql dialect");
        }

    }
}
