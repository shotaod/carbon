package org.carbon.persistent.hibernate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.annotation.Transparent;
import org.carbon.persistent.ConnectionTester;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import static org.carbon.persistent.ConnectionTester.testConnection;

/**
 * @author Shota Oda 2016/11/05.
 */
@Transparent /* managed by PersistentModuleConfigurer */
@Configuration
public class HibernateConfiguration {

    private AtomicInteger count = new AtomicInteger(0);

    @Inject
    private DataSource dataSource;
    @Inject
    private AutoDDL autoDDL;

    @Assemble
    private List<Object> entities;

    @Component
    public EntityManagerFactory entityManagerFactory () {
        PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo();
        PersistenceUnitDescriptor descriptor = new PersistenceUnitInfoDescriptor(persistenceUnitInfo);
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(descriptor, null);
        return entityManagerFactoryBuilder.build();
    }

    private PersistenceUnitInfo persistenceUnitInfo() {
        testConnection(dataSource);
        int count = this.count.getAndIncrement();
        String unitName = "carbon-persistent-" + count;
        List<String> entityFqns = entities.stream().map(entity -> entity.getClass().getName()).collect(Collectors.toList());
        return new DefaultPersistenceUnitInfo(unitName, dataSource, entityFqns, autoDDL.getAction());
    }
}
