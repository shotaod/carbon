package org.carbon.persistent.migrate;

import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2018/01/10.
 */
@Component
public class FlywaySchemaManager extends Flyway implements SchemaManager {
    private Logger logger = LoggerFactory.getLogger(FlywaySchemaManager.class);

    @Assemble
    private DataSource dataSource;

    public FlywaySchemaManager() {
        super();
    }

    protected void setup(List<String> src) {
        setLocations(src.toArray(new String[src.size()]));
        setDataSource(dataSource);
    }

    @Override
    public void manage(List<SchemaAction> actions, List<String> src) {
        for (SchemaAction action : actions) {
            switch (action) {
                case CLEAN:
                    clean();
                case MIGRATE:
                    migrate(src);
                case VALIDATE:
                    validate(src);
                case NONE:
                default:
            }
        }
    }

    @Override
    public void clean() {
        setup(Collections.emptyList());
        logger.info("[schema ] {action: clean}");
        super.clean();
        logger.info("[schema-clean] {action: clean, result: success}");
    }

    @Override
    public void migrate(List<String> directories) {
        setup(directories);
        logger.info("[schema ] {action: migrate, src: {}}", directories);
        int result = migrate();
        logger.info("[schema-migrate] count {}", result);
    }

    @Override
    public void validate(List<String> directories) throws IllegalStateException {
        logger.info("[schema ] {action: validate, src: {}}", directories);
        setup(directories);
        try {
            validate();
        } catch (FlywayException e) {
            throw new IllegalStateException(e);
        }
        logger.info("[schema-validate] result: success");
    }
}
