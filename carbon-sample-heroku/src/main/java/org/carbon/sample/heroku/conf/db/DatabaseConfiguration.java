package org.carbon.sample.heroku.conf.db;

import java.net.URI;
import javax.sql.DataSource;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.postgresql.Driver;

/**
 * @author Shota Oda 2017/02/12.
 */
@Configuration
public class DatabaseConfiguration {
    @Inject
    private DatabaseProp databaseProp;

    @Component
    public DataSource dataSource() {
        URI uri = databaseProp.getUri();
        String username = uri.getUserInfo().split(":")[0];
        String password = uri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath();

        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
