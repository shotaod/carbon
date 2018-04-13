package org.carbon.persistent.conf;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Configuration;
import org.carbon.persistent.ConnectionTester;
import org.carbon.persistent.migrate.SchemaAction;
import org.carbon.persistent.migrate.SchemaManager;
import org.carbon.persistent.prop.PersistentOptionProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota.Oda 2018/01/11.
 */
@Configuration
public class OptionConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(OptionConfiguration.class);

    @Assemble
    private DataSource dataSource;
    @Assemble
    private PersistentOptionProperty optionProperty;
    @Assemble
    private SchemaManager schemaManager;

    @AfterAssemble
    public void afterInject() {
        // connection test
        if (optionProperty.getTest()) {
            logger.info("[persistent] run optional operation !test-connection!. This takes additional time, so you can turn off by setting property{persistent.option.test}=false");
            ConnectionTester.testConnection(dataSource);
        }

        // auto migration
        PersistentOptionProperty.Schema schema = optionProperty.getSchema();
        if (schema != null) {
            List<SchemaAction> actions = schema.getActions().stream()
                    .map(SchemaAction::nameOf)
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.toList());
            logger.info("[persistent] detect auto migration property({}). Run schema action", actions.stream().map(SchemaAction::name).collect(Collectors.joining(",")));
            List<String> src = schema.getSrc();
            schemaManager.manage(actions, src);
        }
    }
}
