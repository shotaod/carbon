package org.carbon.persistent.jooq;

import java.io.File;
import java.sql.Driver;

import org.jooq.SQLDialect;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Target;

/**
 * @author Shota Oda 2016/11/25.
 */
public class JooqGenerator {
    private String url;
    private String user;
    private String password;
    private Class<? extends Driver> driverClass;
    private SQLDialect dialect;
    private String schema;
    private String packageName;
    private File outputDir;

    public JooqGenerator(String url, String user, String password, Class<? extends Driver> driverClass, SQLDialect dialect, String schema, String packageName, File outputDir) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driverClass = driverClass;
        this.dialect = dialect;
        this.schema = schema;
        this.packageName = packageName;
        this.outputDir = outputDir;
    }

    public void generate() throws Exception {
        Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver(driverClass.getName())
                .withUrl(url)
                .withUsername(user)
                .withPassword(password))
            .withGenerator(new Generator()
                .withGenerate(new Generate()
                    .withRelations(true)
                    .withImmutablePojos(false) // if true, cannot use 'into()' method
                    .withInterfaces(true)
                    .withDaos(true))
                .withDatabase(new Database()
                    .withName(getJooqDBClassName())
                    .withIncludes(".*")
                    .withExcludes("")
                    .withInputSchema(schema)
                    .withForcedTypes(new ForcedType()
                        .withUserType("java.time.LocalDateTime")
                        .withConverter("org.carbon.persistent.adhoc.LocalDateTimeConverter")
                        .withTypes("DATETIME"))
                )
                .withTarget(new Target()
                    .withPackageName(packageName)
                    .withDirectory(outputDir.getAbsolutePath())));
        GenerationTool.generate(configuration);
    }

    private String getJooqDBClassName() {
        return String.format("org.jooq.util.%s.%sDatabase", dialect.getNameLC(), dialect.getName());
    }
}
