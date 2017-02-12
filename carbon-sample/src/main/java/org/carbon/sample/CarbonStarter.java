package org.carbon.sample;

import org.carbon.persistent.PersistentModuleConfigurer;
import org.carbon.web.WebStarter;

/**
 * @author Shota Oda 2016/10/02
 */
public class CarbonStarter {
    public static void main(String[] args) throws Exception {
        WebStarter starter = new WebStarter();
        starter.setConfig("config");
        starter.setModuleConfigurers(PersistentModuleConfigurer.class);
        starter.start(ScanBase.class);
    }
}
