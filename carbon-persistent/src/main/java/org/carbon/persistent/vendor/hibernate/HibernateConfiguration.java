package org.carbon.persistent.vendor.hibernate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.annotation.Switch;
import org.carbon.modular.env.EnvironmentMapper;
import org.carbon.persistent.conf.PersistentImplSwitcher;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

/**
 * @author Shota Oda 2016/11/05.
 */
@Switch(PersistentImplSwitcher.class)
@Configuration
public class HibernateConfiguration {
    private static final String AutoDDLKey = "persistent.option.autoddl";
    private static final AtomicInteger count = new AtomicInteger(0);

    @Inject
    private EnvironmentMapper environmentMapper;
    @Inject
    private DataSource dataSource;

    @Assemble({Entity.class})
    private List<Object> entities;

    @Component
    public EntityManagerFactory entityManagerFactory () {
        PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo();
        PersistenceUnitDescriptor descriptor = new PersistenceUnitInfoDescriptor(persistenceUnitInfo);
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(descriptor, null);
        return entityManagerFactoryBuilder.build();
    }

    private PersistenceUnitInfo persistenceUnitInfo() {
        int count = HibernateConfiguration.count.getAndIncrement();
        String unitName = "carbon-persistent-" + count;
        List<String> entityFqns = entities.stream().map(entity -> entity.getClass().getName()).collect(Collectors.toList());
        return new DefaultPersistenceUnitInfo(unitName, dataSource, entityFqns, autoDDL().getAction());
    }

    private DDLAction autoDDL() {
        return environmentMapper
                .findPrimitive(AutoDDLKey, String.class)
                .map(DDLAction::actionOf)
                .orElse(DDLAction.None);
    }
}
