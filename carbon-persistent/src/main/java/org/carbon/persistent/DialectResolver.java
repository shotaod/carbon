package org.carbon.persistent;

/**
 * @author Shota Oda 2017/02/15.
 */
public class DialectResolver {
    public enum Dialect {
        MySql,
        Postgres,
    }
    private static final String MySqlClass = "com.mysql.jdbc.Driver";
    private static final String PostgresClass = "org.postgresql.Driver";
    public static Dialect resolve() {
        boolean isMysql = existDriver(MySqlClass);
        boolean isPostgres = existDriver(PostgresClass);
        if (isMysql && isPostgres) {
            throw new RuntimeException("Dialect Resolve Exception. Multiple Driver found");
        } else if (isMysql) {
            return Dialect.MySql;
        } else if (isPostgres) {
            return Dialect.Postgres;
        } else {
            throw new RuntimeException("Dialect Resolve Exception. No Driver found");
        }
    }

    private static boolean existDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
