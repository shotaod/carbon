package org.carbon.modular;

import java.util.Map;
import java.util.Set;

/**
 * @author Shota Oda 2017/03/04.
 */
public class ModuleDependency {
    private Set<Class<?>> classes;
    private Map<Class<?>, Object> instances;

    public ModuleDependency(Set<Class<?>> classes, Map<Class<?>, Object> instances) {
        this.classes = classes;
        this.instances = instances;
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public Map<Class<?>, Object> getInstances() {
        return instances;
    }
}
