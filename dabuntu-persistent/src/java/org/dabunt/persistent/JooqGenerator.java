package org.dabunt.persistent;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Target;

import java.io.File;

/**
 * @author ubuntu 2016/11/25.
 */
public class JooqGenerator {
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://localhost/dabunt";
		String user = "root";
		Configuration configuration = new Configuration()
			.withJdbc(new Jdbc()
				.withDriver("com.mysql.cj.jdbc.Driver")
				.withUrl(url)
				.withUsername(user)
				.withPassword(""))
			.withGenerator(new Generator()
				.withGenerate(new Generate()
					.withRelations(true)
					.withImmutablePojos(true)
					.withInterfaces(true)
					.withDaos(true))
				.withDatabase(new Database()
					.withName("org.jooq.util.mysql.MySQLDatabase")
					.withIncludes(".*")
					.withExcludes("")
					.withInputSchema("dabunt"))
				.withTarget(new Target()
					.withPackageName("org.dabunt.sample")
					.withDirectory(new File("dabuntu-sample/target/generated-sources/jooq").getAbsolutePath())));
		GenerationTool.generate(configuration);
	}
}
