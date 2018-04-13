package org.carbon.modular;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.carbon.component.meta.ComponentQualifier;
import org.carbon.component.meta.ComponentMetaSet;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleConfigurationResult {
    private ComponentMetaSet componentMetas;
    private Set<Class> scanBases;

    public static ModuleConfigurationResult forScan(Class toScan) {
        return new ModuleConfigurationResult(null, Collections.singleton(toScan));
    }

    public static ModuleConfigurationResult forMetas(ComponentMetaSet metas) {
        return new ModuleConfigurationResult(metas, null);
    }

    public ModuleConfigurationResult(ComponentMetaSet componentMetas, Set<Class> scanBases) {
        this.componentMetas = componentMetas != null ? componentMetas : new ComponentMetaSet();
        this.scanBases = scanBases != null ? scanBases : new HashSet<>();
    }

    public ComponentMetaSet getComponentMetas() {
        return componentMetas;
    }

    public Set<Class> getScanBases() {
        return scanBases;
    }

    public void addComponentExtension(ComponentQualifier extension) {
        componentMetas.addQualifier(extension);
    }

    public ModuleConfigurationResult assign(ModuleConfigurationResult mcr) {
        ComponentMetaSet metaSet = this.componentMetas.assign(mcr.componentMetas);

        Set<Class> scanBasesCopy = new HashSet<>(this.scanBases);
        scanBasesCopy.addAll(mcr.scanBases);

        return new ModuleConfigurationResult(metaSet, scanBasesCopy);
    }
}
