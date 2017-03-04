package org.carbon.modular;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleConfigurationResult {
    private Set<Class<?>> classes;
    private Map<Class<?>, Object> instances;
    private Set<Class> scanBases;

    /**
     * FullControlled ModuleConfiguration Result
     * @param instances instances managed by Module
     * @param classes classes managed by Module for which instantiation is delegated
     * @param scanBases additional scan target class
     */
    public ModuleConfigurationResult(Set<Class<?>> classes, Map<Class<?>, Object> instances, Set<Class> scanBases) {
        this.classes = classes;
        this.instances = instances;
        this.scanBases = scanBases;
    }

    /**
     * Additional Annotation Based Configuration Result
     * @param scanBase scanTargetClass
     */
    public ModuleConfigurationResult(Class<?> scanBase) {
        this.scanBases = Collections.singleton(scanBase);
        this.instances = new HashMap<>();
        this.classes = new HashSet<>();
    }

    public boolean shouldAdditionalScan() {
        return !scanBases.isEmpty();
    }

    public Set<Class> getScanBases() {
        return scanBases;
    }

    public Map<Class<?>, Object> getInstances() {
        return instances;
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public void addClass(Class<?> clazz) {
        this.classes.add(clazz);
    }

    public void addInstance(Class<?> type, Object value) {
        this.instances.put(type, value);
    }

    public void addScanBase(Class scanBase) {
        this.scanBases.add(scanBase);
    }

    public ModuleConfigurationResult assign(ModuleConfigurationResult mcr) {
        HashMap<Class<?>, Object> instancesCopy = new HashMap<>(instances);
        instancesCopy.putAll(mcr.getInstances());

        HashSet<Class<?>> classesCopy = new HashSet<>(this.classes);
        classesCopy.addAll(mcr.getClasses());

        HashSet<Class> scanBasesCopy = new HashSet<>(this.scanBases);
        scanBasesCopy.addAll(mcr.getScanBases());
        return new ModuleConfigurationResult(classesCopy, instancesCopy, scanBasesCopy);
    }
}
