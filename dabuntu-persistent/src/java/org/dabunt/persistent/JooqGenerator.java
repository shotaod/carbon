package org.dabunt.persistent;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.*;

import java.io.File;

/**
 * @author ubuntu 2016/11/25.
 */
public class JooqGenerator {
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://localhost:23306/dabuntdb";
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
					.withInputSchema("dabuntdb")
                    .withForcedTypes(new ForcedType()
                        .withUserType("java.time.LocalDateTime")
                        .withConverter("org.dabunt.persistent.adhoc.LocalDateTimeConverter")
                        .withTypes("DATETIME"))
				)
				.withTarget(new Target()
					.withPackageName("org.dabunt.sample")
					.withDirectory(new File("dabuntu-sample/target/generated-sources/jooq").getAbsolutePath())));
		GenerationTool.generate(configuration);
	}
}
