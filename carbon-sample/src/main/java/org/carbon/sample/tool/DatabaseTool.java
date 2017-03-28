package org.carbon.sample.tool;

import java.io.File;

import com.mysql.jdbc.Driver;
import lombok.Setter;
import org.carbon.persistent.jooq.JooqGenerator;
import org.carbon.util.mapper.PropertyMapper;
import org.jooq.SQLDialect;

/**
 * @author Shota Oda 2017/02/12.
 */
public class DatabaseTool {
    @Setter
    public static class DBProp {
        private String host;
        private String db;
        private String user;
        private String password;
        private Integer port;
    }

    public static void main(String[] args) throws Exception {
        PropertyMapper propertyMapper = new PropertyMapper("config.yml");
        DBProp dbProp = propertyMapper.findAndConstruct("persistent.dataSource", DBProp.class).get();
        String url = String.format("jdbc:mysql://%s:%s/%s", dbProp.host, dbProp.port, dbProp.db);

        new JooqGenerator(
                url,
                dbProp.user,
                dbProp.password,
                Driver.class,
                SQLDialect.MYSQL,
                dbProp.db,
                "org.carbon.sample.ext.jooq",
                new File("carbon-sample/src/main/java")
        ).generate();
    }
}
