package org.carbon.persistent.prop;

/**
 * @author Shota Oda 2016/11/26.
 */
public enum PersistentImplementation {
    Hibernate("hibernate"),
    Jooq("jooq"),
    None("none")
    ;

    private String implementation;

    PersistentImplementation(String implementation) {
        this.implementation = implementation;
    }

    public static PersistentImplementation implOf(String impl) {
        for (PersistentImplementation value : values()) {
            if (value.implementation.equals(impl)) {
                return value;
            }
        }
        return None;
    }
}
