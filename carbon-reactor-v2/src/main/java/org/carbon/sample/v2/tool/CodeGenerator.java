package org.carbon.sample.v2.tool;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Setter;
import org.carbon.persistent.dialect.Dialect;
import org.carbon.persistent.vendor.jooq.JooqCodeGenerator;
import org.carbon.util.mapper.YamlObjectMapper;

/**
 * @author Shota Oda 2017/02/12.
 */
public class CodeGenerator {
    private static final String fileName = "config.local.yml";

    @Setter
    public static class DBProp {
        private String host;
        private String db;
        private String user;
        private String password;
        private Integer port;
    }

    public static void main(String[] args) throws Exception {
        String rootDir = System.getProperty("user.dir");
        Path path = Paths.get(rootDir, "carbon-sample-v2/src/main/resources", fileName);
        YamlObjectMapper yamlMapper = new YamlObjectMapper(path);
        DBProp p = yamlMapper.map("persistent.dataSource", DBProp.class);
        String distDir = "carbon-sample-v2/src/main/java/";
        String distPackage = "org.carbon.sample.v2.ext.jooq";
        JooqCodeGenerator
                .configure(
                        p.host,
                        p.port,
                        p.user,
                        p.password,
                        p.db,
                        Dialect.MySql,
                        distPackage,
                        new File(distDir))
                .withExclusion("flyway_schema_history")
                .generate();
    }
}
