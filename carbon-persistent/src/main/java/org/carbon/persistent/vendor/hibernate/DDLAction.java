package org.carbon.persistent.vendor.hibernate;

/**
 * @author Shota Oda 2017/02/10.
 */
public enum DDLAction {
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

    DDLAction(String action) {
        this.action = action;
    }
    public static DDLAction actionOf(String action) {
        for (DDLAction ddl: values()) {
            if (ddl.action.equals(action)) {
                return ddl;
            }
        }
        return None;
    }
}
