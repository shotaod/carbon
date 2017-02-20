package org.carbon.sample.heroku.conf.db;

import java.net.URI;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.exception.SetupException;
import org.carbon.util.mapper.ConfigHolder;

/**
 * @author Shota Oda 2017/02/12.
 */
@Configuration
public class DatabaseConfiguration {
    private final String propKey = "sample.heroku.db";
    @Inject
    private ConfigHolder configHolder;

    @Component
    public DataSource dataSource() {
        DatabaseProp prop = configHolder.findOne(propKey, DatabaseProp.class).orElseThrow(() -> new SetupException(String.format("Fail to find props[%s]", propKey)));
        URI uri = prop.getUri();
        String username = uri.getUserInfo().split(":")[0];
        String password = uri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath();

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
