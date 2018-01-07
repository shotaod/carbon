package org.carbon.persistent;

import org.carbon.modular.ModuleConfigurationResult;
import org.carbon.modular.ModuleConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/02/09.
 */
public class PersistentModuleConfigurer implements ModuleConfigurer {
    private Logger logger = LoggerFactory.getLogger(PersistentModuleConfigurer.class);

    @Override
    public ModuleConfigurationResult configure() {
        Class<PersistentScanBase> scanBaseClass = PersistentScanBase.class;
        logger.debug("add scan target class [{}]", scanBaseClass);
        return new ModuleConfigurationResult(scanBaseClass);
    }
}
