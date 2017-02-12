package org.carbon.persistent.hibernate;

/**
 * @author Shota Oda 2017/02/10.
 */
public enum AutoDDL {
    None("none"),
    CreateOnly("create-only"),
    Drop("drop"),
    Create("create"),
    Validate("validate"),
    Update("update"),
    ;

    private String action;
    public String getAction() {
        return action;
    }

    AutoDDL(String action) {
        this.action = action;
    }
    public static AutoDDL actionOf(String action) {
        for (AutoDDL ddl: values()) {
            if (ddl.action.equals(action)) {
                return ddl;
            }
        }
        return None;
    }
}
