package org.carbon.persistent.config;


import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * this class is for config mapping
 * @author Shota Oda 2016/11/13.
 */
public class DataSourceConfig {

    private String dialect;
    private String host;
    private String db;
    private String user;
    private String password;
    private Integer port;

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setDb(String db) {
        this.db = db;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPort(Integer port) {
        this.port = port;
    }

    public DataSource toDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        String dbUrl = String.format("jdbc:%s://%s:%s/%s", dialect, host, port, db);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}
