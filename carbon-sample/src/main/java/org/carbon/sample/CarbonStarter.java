package org.carbon.sample;

import org.carbon.persistent.PersistentModuleConfigurer;
import org.carbon.web.CarbonApplicationStarter;

/**
 * @author Shota Oda 2016/10/02
 */
public class CarbonStarter {
    public static void main(String[] args) throws Exception {
        CarbonApplicationStarter starter = new CarbonApplicationStarter();
        starter.addModuleConfigurers(PersistentModuleConfigurer.class);
        starter.start(ScanBase.class);
    }
}
