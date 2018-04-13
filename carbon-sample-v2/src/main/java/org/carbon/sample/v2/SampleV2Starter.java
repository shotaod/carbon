package org.carbon.sample.v2;

import org.carbon.authentication.module.AuthenticationModuleConfigurer;
import org.carbon.persistent.PersistentModuleConfigurer;
import org.carbon.web.CarbonApplicationStarter;

/**
 * @author Shota Oda 2017/02/12
 */
public class SampleV2Starter {
    public static void main(String[] args) throws Exception {
        CarbonApplicationStarter starter = new CarbonApplicationStarter();
        starter.addModuleConfigurers(
                PersistentModuleConfigurer.class,
                AuthenticationModuleConfigurer.class
        );
        starter.start(ScanBase.class);
    }
}
