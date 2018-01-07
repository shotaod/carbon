package org.carbon.modular;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleConfigurationResult {
    private ComponentMetaSet componentMetas;
    private Set<Class> scanBases;

    /**
     * FullControlled ModuleConfiguration Result
     *
     * @param componentMetas componentMeta configured by ModuleConfigurer
     * @param scanBases      additional scan target class
     */
    public ModuleConfigurationResult(Set<ComponentMeta> componentMetas, Set<Class> scanBases) {
        this.componentMetas = new ComponentMetaSet(componentMetas);
        this.scanBases = scanBases;
    }

    /**
     * Additional Annotation Based Configuration Result
     *
     * @param scanBase scanTargetClass
     */
    public ModuleConfigurationResult(Class<?> scanBase) {
        this.componentMetas = new ComponentMetaSet();
        this.scanBases = Collections.singleton(scanBase);
    }

    public ComponentMetaSet getComponentMetas() {
        return componentMetas;
    }

    public Set<Class> getScanBases() {
        return scanBases;
    }

    public ModuleConfigurationResult assign(ModuleConfigurationResult mcr) {
        ComponentMetaSet metaSet = this.componentMetas.assign(mcr.componentMetas);

        HashSet<Class> scanBasesCopy = new HashSet<>(this.scanBases);
        scanBasesCopy.addAll(mcr.getScanBases());

        return new ModuleConfigurationResult(metaSet, scanBasesCopy);
    }
}
