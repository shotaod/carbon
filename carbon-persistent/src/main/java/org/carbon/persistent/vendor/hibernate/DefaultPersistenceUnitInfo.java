package org.carbon.persistent.vendor.hibernate;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.jpa.HibernatePersistenceProvider;

/**
 * @author Shota Oda 2017/02/10.
 */
public class DefaultPersistenceUnitInfo implements PersistenceUnitInfo {
    private String unitName;
    private DataSource dataSource;
    private List<String> entityClassNames;
    private String autoddl = "create";

    public DefaultPersistenceUnitInfo(String unitName, DataSource dataSource, List<String> entityClassNames, String autoddl) {
        this.unitName = unitName;
        this.dataSource = dataSource;
        this.entityClassNames = entityClassNames;
        this.autoddl = autoddl;
    }

    @Override
    public String getPersistenceUnitName() {
        return unitName;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return HibernatePersistenceProvider.class.getName();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        // manage by carbon or developer
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return dataSource;
    }

    @Override
    public List<String> getMappingFileNames() {
        // dabunt does not use mapping file
        return null;
    }

    @Override
    public List<URL> getJarFileUrls() {
        // dabunt does not use external jar file
        return null;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        // dabunt config by programmatically
        // return null caz not use persistence.xml
        return null;
    }

    @Override
    public List<String> getManagedClassNames() {
        // entity class list
        return entityClassNames;
    }

    @Override
    public boolean excludeUnlistedClasses() {
        // maybe... only use getManagedClassNames() or not(?)
        return true;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        // for develop
        return SharedCacheMode.NONE;
        // for production
        // return SharedCacheMode.ENABLE_SELECTIVE;
    }

    @Override
    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    @Override
    public Properties getProperties() {
        // additional property mainly for hibernate
        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect", MySQLDialect.class.getName());
        prop.setProperty("hibernate.format_sql", "true");
        prop.setProperty("hibernate.hbm2ddl.auto", autoddl);
        return prop;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "1.0";
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}
