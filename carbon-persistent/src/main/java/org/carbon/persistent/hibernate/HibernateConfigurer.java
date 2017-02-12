package org.carbon.persistent.hibernate;

import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

/**
 * @author Shota Oda 2016/11/05.
 */
@Configuration
public class HibernateConfigurer {

    private AtomicInteger count = new AtomicInteger(0);

    @Inject
    private DataSource dataSource;
    @Inject
    private EntitiesInfo entitiesInfo;
    @Inject
    private AutoDDL autoDDL;

    @Component
    public EntityManagerFactory entityManagerFactory () {
        PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo();
        PersistenceUnitDescriptor descriptor = new PersistenceUnitInfoDescriptor(persistenceUnitInfo);
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(descriptor, null);
        return entityManagerFactoryBuilder.build();
    }

    private PersistenceUnitInfo persistenceUnitInfo() {
        int count = this.count.getAndIncrement();
        String unitName = "carbon-persistent-" + count;

        return new DefaultPersistenceUnitInfo(unitName, dataSource, entitiesInfo.getEntityFqns(), autoDDL.getAction());
    }
}
