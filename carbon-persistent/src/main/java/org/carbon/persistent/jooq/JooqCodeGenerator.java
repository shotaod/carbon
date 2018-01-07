package org.carbon.persistent.jooq;

import java.io.File;
import java.time.LocalDateTime;

import org.carbon.persistent.adhoc.LocalDateTimeConverter;
import org.carbon.persistent.dialect.Dialect;
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
public class JooqCodeGenerator {
    private String host;
    private Integer port;
    private String user;
    private String password;
    private String db;
    private Dialect dialect;
    private String packageName;
    private File outputDir;

    private JooqCodeGenerator() {
    }

    public static JooqCodeGenerator configure(
            String host,
            Integer port,
            String user,
            String password,
            String db,
            Dialect dialect,
            String packageName,
            File outputDir
    ) {
        JooqCodeGenerator self = new JooqCodeGenerator();
        self.host = host;
        self.port = port;
        self.user = user;
        self.password = password;
        self.db = db;
        self.dialect = dialect;
        self.packageName = packageName;
        self.outputDir = outputDir;
        return self;
    }

    public void generate() throws Exception {
        Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver(dialect.getDriverClassName())
                .withUrl(getUrl())
                .withUsername(user)
                .withPassword(password))
            .withGenerator(new Generator()
                .withName(CarbonJooqGenerator.class.getName())
                .withGenerate(new Generate()
                    .withRelations(true)
                    .withImmutablePojos(false) // if true, cannot use 'into()' method
                    .withInterfaces(true)
                    .withDaos(true))
                .withDatabase(new Database()
                    .withName(getJooqDBClassName())
                    .withIncludes(".*")
                    .withExcludes("")
                    .withInputSchema(db)
                    .withForcedTypes(new ForcedType()
                        .withUserType(LocalDateTime.class.getName())
                        .withConverter(LocalDateTimeConverter.class.getName())
                        .withTypes("DATETIME"))
                )
                .withTarget(new Target()
                    .withPackageName(packageName)
                    .withDirectory(outputDir.getAbsolutePath())));
        GenerationTool.generate(configuration);
    }

    private String getUrl() {
        return String.format("jdbc:%s://%s:%s/%s", dialect.getProtocol(), host, port, db);
    }

    private String getJooqDBClassName() {
        SQLDialect dialect = getJooqSqlDialect();
        return String.format("org.jooq.util.%s.%sDatabase", dialect.getNameLC(), dialect.getName());
    }

    private SQLDialect getJooqSqlDialect() {
        switch (this.dialect) {
            case MySql:
                return SQLDialect.MYSQL;
            case Postgres:
                return SQLDialect.POSTGRES;
        }
        throw new UnsupportedOperationException();
    }
}
