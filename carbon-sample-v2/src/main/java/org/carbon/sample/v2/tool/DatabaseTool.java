package org.carbon.sample.v2.tool;

import java.io.File;

import lombok.Setter;
import org.carbon.persistent.dialect.Dialect;
import org.carbon.persistent.jooq.JooqCodeGenerator;
import org.carbon.sample.v2.ext.jooq.Carbon;
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
        YamlObjectMapper yamlMapper = new YamlObjectMapper("config.local.yml");
        DBProp p = yamlMapper.map("persistent.datasource", DBProp.class);
        String dist = "carbon-sample-v2/src/main/java/";
        JooqCodeGenerator
                .configure(
                        p.host,
                        p.port,
                        p.user,
                        p.password,
                        p.db,
                        Dialect.MySql,
                        Carbon.class.getPackage().getName(),
                        new File(dist))
                .generate();
    }
}
