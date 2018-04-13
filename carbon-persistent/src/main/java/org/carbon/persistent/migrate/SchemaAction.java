package org.carbon.persistent.migrate;

/**
 * @author Shota Oda 2018/01/10.
 */
public enum SchemaAction {
    CLEAN("clean", 1),
    MIGRATE("migrate", 2),
    VALIDATE("validate", 3),
    NONE("none", 0),
    ;

    private String name;
    private int priority;

    SchemaAction(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public static SchemaAction nameOf(String name) {
        for (SchemaAction action : values()) {
            if (action.name.equals(name)) {
                return action;
            }
        }
        return NONE;
    }
}
