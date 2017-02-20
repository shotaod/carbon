package org.carbon.sample.heroku;

import java.util.Optional;

import org.carbon.persistent.PersistentModuleConfigurer;
import org.carbon.web.WebStarter;

/**
 * @author Shota Oda 2017/02/12
 */
public class SampleHerokuStarter {
    public static void main(String[] args) throws Exception {
        WebStarter starter = new WebStarter();
        starter.setConfig(getConf());
        starter.setModuleConfigurers(PersistentModuleConfigurer.class);
        starter.start(ScanBase.class);
    }

    private static String getConf() {
        System.out.println(System.getProperty("running"));
        return Optional.ofNullable(System.getProperty("running")).map(running -> {
            switch (running) {
                case "local":
                    return "config-local";
                case "heroku":
                    return "config-heroku";
                default:
                    return "config";
            }
        }).orElse("config");
    }
}
