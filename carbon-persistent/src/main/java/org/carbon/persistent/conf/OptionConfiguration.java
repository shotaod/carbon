package org.carbon.persistent.conf;

import javax.sql.DataSource;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.persistent.ConnectionTester;
import org.carbon.persistent.migrate.SchemaAction;
import org.carbon.persistent.migrate.SchemaManager;
import org.carbon.persistent.prop.PersistentOptionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author garden 2018/01/11.
 */
@Configuration
public class OptionConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(OptionConfiguration.class);

    @Inject
    private DataSource dataSource;
    @Inject
    private PersistentOptionProperty optionProperty;
    @Inject
    private SchemaManager schemaManager;

    @AfterInject
    public void afterInject() {
        // connection test
        if (optionProperty.getTest()) {
            logger.info("[persistent] run optional operation !test-connection!. This takes a time. To turn off, Set property{persistent.option.test}=false");
            ConnectionTester.testConnection(dataSource);
        }

        // auto migration
        PersistentOptionProperty.Schema schema = optionProperty.getSchema();
        schema.getActions().stream()
                .map(SchemaAction::nameOf)
                .sorted()
                .forEach(schemaManager.ready(schema.getSrc())::call);
    }
}
