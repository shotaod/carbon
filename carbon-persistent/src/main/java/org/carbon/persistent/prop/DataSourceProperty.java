package org.carbon.persistent.prop;


import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * this class is for config mapping
 *
 * @author Shota Oda 2016/11/13.
 */
public class DataSourceProperty {

    private String dialect;
    private String host;
    private String db;
    private String user;
    private String password;
    private Integer port;
    private Map<String, String> params;

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

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public DataSource toDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        String dbUrl = String.format("jdbc:%s://%s:%s", dialect, host, port);
        if (params != null) {
            String query = params.entrySet().stream()
                    .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                    .collect(Collectors.joining("&", "?", ""));
            dbUrl += query;
        }
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}
