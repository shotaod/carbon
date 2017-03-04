package org.carbon.authentication;

import org.carbon.modular.ModuleConfigurationResult;
import org.carbon.modular.ModuleConfigurer;
import org.carbon.util.mapper.ConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/03/04.
 */
public class AuthenticationModuleConfigurer implements ModuleConfigurer {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationModuleConfigurer.class);

    @Override
    public ModuleConfigurationResult configure(Class scanBase, ConfigHolder configHolder) {
        Class<AuthenticationScanBase> scanBaseClass = AuthenticationScanBase.class;
        logger.debug("add scan target class [{}]", scanBaseClass);
        return new ModuleConfigurationResult(scanBaseClass);
    }
}
