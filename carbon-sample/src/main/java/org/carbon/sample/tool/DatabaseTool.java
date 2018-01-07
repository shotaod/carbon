package org.carbon.sample.tool;

import java.io.File;

import lombok.Setter;
import org.carbon.persistent.dialect.Dialect;
import org.carbon.persistent.jooq.JooqCodeGenerator;
import org.carbon.util.mapper.YamlObjectMapper;

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
        YamlObjectMapper propertyMapper = new YamlObjectMapper("config.yml");
        DBProp p = propertyMapper.map("persistent.dataSource", DBProp.class);

        JooqCodeGenerator
                .configure(
                        p.host,
                        p.port,
                        p.user,
                        p.password,
                        p.db,
                        Dialect.MySql,
                        "org.carbon.sample.ext.jooq",
                        new File("carbon-sample/src/main/java")
                ).generate();
    }
}
