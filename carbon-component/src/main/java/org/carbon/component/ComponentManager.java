package org.carbon.component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.exception.IllegalDependencyException;
import org.carbon.component.exception.PackageScanException;
import org.carbon.component.construct.ClassConstructor;
import org.carbon.component.inject.DependencyInjector;
import org.carbon.component.scan.TargetBaseScanner;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.format.StringLineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/07.
 */
public class ComponentManager {
    private Logger logger = LoggerFactory.getLogger(ComponentManager.class);

    private TargetBaseScanner scanner = new TargetBaseScanner();
    private ClassConstructor generator = new ClassConstructor();
    private DependencyInjector injector  = new DependencyInjector();

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

        if (logger.isInfoEnabled()) {
            loggingConfiguration(configurations);
        }

        while (!configurations.isEmpty()) {
            Map<Class, Object> tmp = injector.injectOnlySatisfied(configurations, instances).entrySet().stream()
                .peek(entry -> configurations.remove(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
            if (tmp.isEmpty()) {
                throwIllegalDependencyException(configurations);
            }
            Map<Class, Object> instancesSuppliedByConfiguration = generator.generateMethodComponent(tmp);
            instances.putAll(instancesSuppliedByConfiguration);
        }

        Map<Class, Object> injected = injector.injectEach(instances);
        if (logger.isDebugEnabled()) {
            loggingDependencies(injected);
        }
        return injected.entrySet().stream()
            // exclude dependency
            .filter(e -> !dependency.containsKey(e.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("unchecked")
    public <T> T constructClass(Class<T> clazz) {
        return (T)generator.constructClass(clazz);
    }


    private void throwIllegalDependencyException(Map<Class, Object> configurations) {
        String message = "Fail to resolve dependency\ncheck below classes\n"
                + configurations.keySet().stream().map(Class::getName).collect(Collectors.joining("\n"));
        throw new IllegalDependencyException(message);
    }

    // ===================================================================================
    //                                                                          Logging
    //                                                                          ==========

    private void loggingConfiguration(Map<Class, Object> configurations) {
        String confClassNames = configurations.entrySet().stream().map(entry -> "- " + entry.getKey().getName()).sorted(String::compareTo).collect(Collectors.joining("\n"));
        String mes = ChapterAttr.getBuilder("Detect Configurations class below")
                .appendLine(confClassNames).toString();
        logger.info(mes);
    }

    public void loggingDependencies(Map<Class, Object> instances) {
        StringLineBuilder sb = ChapterAttr.getBuilder("Injection Result");
        instances.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().getName().toLowerCase().compareTo(e2.getKey().getName().toLowerCase()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (can1, can2) -> can1,
                        LinkedHashMap::new))
                .forEach((k, v) -> {
                    this.showDependencies(sb, k, 0);
                });
        logger.debug(sb.toString());
    }

    private StringLineBuilder showDependencies(StringLineBuilder sb, Class clazz, Integer depth) {
        String space = Stream.generate(() -> "    ").limit(depth).collect(Collectors.joining());
        if (depth > 0) {
            space += (" └─ ");
        }
        sb.appendLine(space + clazz.getName());
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    showDependencies(sb, field.getType(), depth + 1);
                });
        return sb;
    }
}
