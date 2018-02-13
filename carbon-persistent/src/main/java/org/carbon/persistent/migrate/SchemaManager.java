package org.carbon.persistent.migrate;

import java.util.List;

/**
 * @author garden 2018/01/11.
 */
public interface SchemaManager {
    @FunctionalInterface
    interface Callable {
        void call(SchemaAction action);
    }

    Callable ready(List<String> src);

    void manage(SchemaAction action, List<String> src);

    void clean();

    void migrate(List<String> src);

    void validate(List<String> src) throws IllegalStateException;
}
