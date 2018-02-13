package org.carbon.persistent.migrate;

import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
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

    @Inject
    private DataSource dataSource;

    public FlywaySchemaManager() {
        super();
    }

    protected void setup(List<String> src) {
        setLocations(src.toArray(new String[src.size()]));
        setDataSource(dataSource);
    }

    @Override
    public Callable ready(List<String> src) {
        return action -> this.manage(action, src);
    }

    @Override
    public void manage(SchemaAction action, List<String> src) {
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

    @Override
    public void clean() {
        setup(Collections.emptyList());
        logger.info("[schema ] {action: clean}");
        super.clean();
        logger.info("[schema ] {action: clean, result: success}");
    }

    @Override
    public void migrate(List<String> src) {
        setup(src);
        logger.info("[schema ] {action: migrate, src: {}}", src);
        int result = migrate();
        logger.info("[schema-migrate] count{}", result);
    }

    @Override
    public void validate(List<String> src) throws IllegalStateException {
        logger.info("[schema ] {action: validate, src: {}}", src);
        setup(src);
        try {
            validate();
        } catch (FlywayException e) {
            throw new IllegalStateException(e);
        }
        logger.info("[schema-validate] result: success");
    }
}
