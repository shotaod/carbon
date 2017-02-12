package org.carbon.component;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.exception.IllegalDependencyException;
import org.carbon.component.exception.PackageScanException;
import org.carbon.component.generator.InstanceGenerator;
import org.carbon.component.inject.InstanceInjector;
import org.carbon.component.scan.TargetBaseScanner;

/**
 * @author Shota Oda 2016/10/07.
 */
public class ComponentManager {

    private TargetBaseScanner scanner = new TargetBaseScanner();
    private InstanceGenerator generator = new InstanceGenerator();
    private InstanceInjector injector  = new InstanceInjector();

    public Set<Class<?>> scanComponent (Class scanBase) throws PackageScanException {
        return scanner.scan(scanBase, Collections.singleton(Component.class));
    }
    public Set<Class<?>> scan(Class scanBase, Set<Class<? extends Annotation>> scanTargets) throws PackageScanException {
        return scanner.scan(scanBase, scanTargets);
    }

    @SuppressWarnings("unchecked")
    public Map<Class, Object> generate(Set<Class> classes, Map<Class,Object> dependency) throws Exception{
        Map<Class, Object> instances = generator.generate(classes);
        instances.putAll(dependency);
        /*
        * 1. Inject Configuration <- OtherInstance
        * 2. Generate Component by Configuration Method
        * 3. Inject each other
        */
        // 1.
        Map<Class, Object> configurations = instances.entrySet().stream()
            .filter(entry -> entry.getKey().isAnnotationPresent(Configuration.class))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
        while (!configurations.isEmpty()) {
            Map<Class, Object> tmp = injector.injectOnlySatisfied(configurations, instances).entrySet().stream()
                .peek(entry -> configurations.remove(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
            if (tmp.isEmpty()) {
                String message =
                        "Fail to resolve dependency\ncheck below classes\n"+
                        configurations.keySet().stream().map(Class::getName).collect(Collectors.joining("- ","","\n"));
                throw new IllegalDependencyException(message);
            }
            Map<Class, Object> instancesSuppliedByConfiguration = generator.generateMethodComponent(tmp);
            instances.putAll(instancesSuppliedByConfiguration);
        }

        Map<Class, Object> injected = injector.injectEach(instances);
        return injected.entrySet().stream()
            // exclude dependency
            .filter(e -> !dependency.containsKey(e.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("unchecked")
    public <T> T constructClass(Class<T> clazz) {
        return (T)generator.constructClass(clazz);
    }
}
