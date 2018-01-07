package org.carbon.modular;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.carbon.component.ComponentFactory;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.carbon.modular.exception.ModuleConfigureException;
import org.carbon.util.constructor.NoArgsConstructorUtil;
import org.carbon.util.exception.ConstructionException;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleConfigurerResolver {
    public ComponentMetaSet resolve(Set<Class<? extends ModuleConfigurer>> moduleConfigurerClasses) {
        ComponentFactory componentFactory = new ComponentFactory();
        ModuleConfigurationResult additionalModuleConfiguration = moduleConfigurerClasses.stream()
                .map(this::construct)
                .map(ModuleConfigurer::configure)
                .reduce(ModuleConfigurationResult::assign)
                .orElseThrow(() -> new ModuleConfigureException("Fail to configure module, Not found ModuleConfigurers"));

        ModuleConfigurationResult moduleConfiguration = additionalModuleConfiguration.assign(new ModuleConfigurationResult(ModulerScanBase.class));

        Set<ComponentMeta> scannedMeta = moduleConfiguration.getScanBases().stream()
                .flatMap(moduleScanBase -> componentFactory.scanComponent(moduleScanBase).stream())
                .map(ComponentMeta::noImpl)
                .collect(Collectors.toSet());

        return moduleConfiguration
                .assign(new ModuleConfigurationResult(scannedMeta, Collections.emptySet()))
                .getComponentMetas();
    }

    private <T extends ModuleConfigurer> ModuleConfigurer construct(Class<T> configurerClass) {
        try {
            return NoArgsConstructorUtil.construct(configurerClass);
        } catch (ConstructionException e) {
            throw new ModuleConfigureException("Fail to configure module, Fail to instantiate class[" + configurerClass + "]", e);
        }
    }
}
