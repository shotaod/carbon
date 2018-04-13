package org.carbon.persistent.migrate;

import java.util.List;

/**
 * @author Shota.Oda 2018/01/11.
 */
public interface SchemaManager {

    /**
     * Iterate actions,
     *
     * @param actions not null
     * @param directories     null allowed
     */
    void manage(List<SchemaAction> actions, List<String> directories);

    /**
     * clean schema
     */
    void clean();

    /**
     * migrate schema(directories)
     *
     * @param directories schema
     */
    void migrate(List<String> directories);

    /**
     * validate schema
     *
     * @param directories schema
     * @throws IllegalStateException if validation fail
     */
    void validate(List<String> directories) throws IllegalStateException;
}
