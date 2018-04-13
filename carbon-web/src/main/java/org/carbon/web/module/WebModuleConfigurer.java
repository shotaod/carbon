package org.carbon.web.module;

import org.carbon.modular.ModuleConfigurationResult;
import org.carbon.modular.ModuleConfigurer;
import org.carbon.web.CarbonWebScanBase;
import org.carbon.modular.qua.PropertyInjectQualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2018/01/10.
 */
public class WebModuleConfigurer implements ModuleConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebModuleConfigurer.class);

    @Override
    public ModuleConfigurationResult configure() {
        Class<CarbonWebScanBase> scanBaseClass = CarbonWebScanBase.class;
        logger.debug("add scan target class [{}]", scanBaseClass);
        ModuleConfigurationResult result = ModuleConfigurationResult.forScan(scanBaseClass);
        result.addComponentExtension(new PropertyInjectQualifier());
        return result;
    }
}
