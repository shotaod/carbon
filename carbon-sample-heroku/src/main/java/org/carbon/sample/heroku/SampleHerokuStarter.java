package org.carbon.sample.heroku;

import java.util.Optional;

import org.carbon.authentication.AuthenticationModuleConfigurer;
import org.carbon.persistent.PersistentModuleConfigurer;
import org.carbon.web.CarbonApplicationStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/02/12
 */
public class SampleHerokuStarter {
    private static Logger logger = LoggerFactory.getLogger(SampleHerokuStarter.class);
    public static void main(String[] args) throws Exception {
        CarbonApplicationStarter starter = new CarbonApplicationStarter();
        starter.setConfig(getConf());
        starter.addModuleConfigurers(
            PersistentModuleConfigurer.class,
            AuthenticationModuleConfigurer.class
        );
        starter.start(ScanBase.class);
    }

    private static String getConf() {
        String confProperty = System.getProperty("running");
        logger.info("Running Mode [{}]", confProperty);
        return Optional.ofNullable(System.getProperty("running")).map(running -> {
            switch (confProperty) {
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
