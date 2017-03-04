package org.carbon.modular;

import java.util.List;

import org.carbon.component.ComponentManager;
import org.carbon.modular.exception.ModuleConfigureException;
import org.carbon.util.mapper.ConfigHolder;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleConfigurerResolver {
    public ModuleDependency resolve(List<Class<? extends ModuleConfigurer>> moduleConfigurerClasses, Class scanBase ,ConfigHolder configHolder) {
        ComponentManager componentManager = new ComponentManager();
        ModuleConfigurationResult moduleConfigurationResult = moduleConfigurerClasses.stream()
                .map(componentManager::constructClass)
                .map(moduleConfigurer -> moduleConfigurer.configure(scanBase, configHolder))
                .reduce(ModuleConfigurationResult::assign)
                .orElseThrow(() -> new ModuleConfigureException("Fail to configurer module, Not found ModuleConfigurers"));

        moduleConfigurationResult.getScanBases().stream()
            .flatMap(moduleScanBase -> componentManager.scanComponent(moduleScanBase).stream())
            .forEach(moduleConfigurationResult::addClass);

        return new ModuleDependency(moduleConfigurationResult.getClasses(), moduleConfigurationResult.getInstances());
    }
}
