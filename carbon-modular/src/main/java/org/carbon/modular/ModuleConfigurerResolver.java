package org.carbon.modular;

import java.util.Set;
import java.util.stream.Stream;

import org.carbon.component.HandyComponentManager;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.carbon.modular.exception.ModuleConfigureException;
import org.carbon.util.constructor.NoArgsConstructorUtil;
import org.carbon.util.exception.ConstructionException;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleConfigurerResolver {
    private Set<Class<? extends ModuleConfigurer>> moduleConfigurerClasses;
    private Stream<ModuleConfigurer> moduleConfigurers;

    public ModuleConfigurerResolver(Set<Class<? extends ModuleConfigurer>> moduleConfigurerClasses, ModuleConfigurer... moduleConfigurers) {
        this.moduleConfigurerClasses = moduleConfigurerClasses;
        this.moduleConfigurers = Stream.of(moduleConfigurers);
    }

    public ComponentMetaSet resolve() {
        HandyComponentManager componentManager = new HandyComponentManager();
        Stream<ModuleConfigurer> moduleConfigurerStream = moduleConfigurerClasses.stream()
                .map(this::construct);
        ModuleConfigurationResult additionalModuleConfiguration = Stream.concat(moduleConfigurerStream, moduleConfigurers)
                .map(ModuleConfigurer::configure)
                .reduce(ModuleConfigurationResult::assign)
                .orElseThrow(() -> new ModuleConfigureException("Fail to configure module, Not found ModuleConfigurers"));

        ModuleConfigurationResult moduleModuleConfigurer = ModuleConfigurationResult.forScan(ModulerScanBase.class);
        ModuleConfigurationResult moduleConfiguration = additionalModuleConfiguration.assign(moduleModuleConfigurer);

        ComponentMetaSet scannedMeta = moduleConfiguration.getScanBases().stream()
                .flatMap(moduleScanBase -> componentManager.scanComponent(moduleScanBase).stream())
                .map(ComponentMeta::noImpl)
                .collect(ComponentMetaSet.Collectors.toSet());
        ModuleConfigurationResult scannedConfigurer = ModuleConfigurationResult.forMetas(scannedMeta);

        return moduleConfiguration
                .assign(scannedConfigurer)
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
