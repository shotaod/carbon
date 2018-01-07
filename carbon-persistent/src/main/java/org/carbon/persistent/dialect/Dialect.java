package org.carbon.persistent.dialect;

/**
 * @author Shota Oda 2018/01/02.
 */
public enum Dialect {
    MySql("mysql", "com.mysql.cj.jdbc.Driver"),
    Postgres("postgresql", "org.postgersql,Driver");
    private String protocol;
    private String driverClassName;

    Dialect(String protocol, String driverClassName) {
        this.protocol = protocol;
        this.driverClassName = driverClassName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
