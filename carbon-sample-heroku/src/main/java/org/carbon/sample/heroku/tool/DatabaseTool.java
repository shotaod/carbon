package org.carbon.sample.heroku.tool;

import java.io.File;

import org.carbon.persistent.jooq.JooqGenerator;
import org.jooq.SQLDialect;
import org.postgresql.Driver;

/**
 * @author Shota Oda 2017/02/12.
 */
public class DatabaseTool {
    private static String host = "carbon.heroku.rdb";
    private static String db = "d6vh9houtm9ttn";
    private static String user = "root";
    private static String password = "password";
    private static Integer port = 25432;

    public static void main(String[] args) throws Exception {
        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
        String dist = "carbon-sample-heroku/src/main/java/";
        new JooqGenerator(
                url,
                user,
                password,
                Driver.class,
                SQLDialect.POSTGRES,
                db,
                "org.carbon.sample.heroku.ext.jooq",
                new File(dist)
        ).generate();
    }
}
