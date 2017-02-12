package org.carbon.persistent.jooq;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.*;

import java.io.File;

/**
 * @author Shota Oda 2016/11/25.
 */
public class JooqGenerator {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:23306/carbondb";
        String user = "root";
        String password = "password";
        Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver("com.mysql.cj.jdbc.Driver")
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
                    .withName("org.jooq.util.mysql.MySQLDatabase")
                    .withIncludes(".*")
                    .withExcludes("")
                    .withInputSchema("carbondb")
                    .withForcedTypes(new ForcedType()
                        .withUserType("java.time.LocalDateTime")
                        .withConverter("org.carbon.persistent.adhoc.LocalDateTimeConverter")
                        .withTypes("DATETIME"))
                )
                .withTarget(new Target()
                    .withPackageName("org.carbon.sample")
                    .withDirectory(new File("carbon-sample/target/generated-sources/jooq").getAbsolutePath())));
        GenerationTool.generate(configuration);
    }
}
