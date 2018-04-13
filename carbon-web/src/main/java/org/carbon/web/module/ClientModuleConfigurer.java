package org.carbon.web.module;

import org.carbon.modular.ModuleConfigurationResult;
import org.carbon.modular.ModuleConfigurer;

/**
 * @author Shota Oda 2018/01/11.
 */
public class ClientModuleConfigurer implements ModuleConfigurer {
    private Class scanBase;

    public ClientModuleConfigurer(Class scanBase) {
        this.scanBase = scanBase;
    }

    @Override
    public ModuleConfigurationResult configure() {
        return ModuleConfigurationResult.forScan(scanBase);
    }
}
