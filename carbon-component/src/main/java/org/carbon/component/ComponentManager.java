package org.carbon.component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.construct.ClassConstructor;
import org.carbon.component.exception.IllegalDependencyException;
import org.carbon.component.exception.MethodInvocationException;
import org.carbon.component.exception.PackageScanException;
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
    private DependencyInjector injector = new DependencyInjector();

    public Set<Class<?>> scanComponent(Class scanBase) throws PackageScanException {
        return scanner.scan(scanBase, Collections.singleton(Component.class));
    }

    public Set<Class<?>> scan(Class scanBase, Set<Class<? extends Annotation>> scanTargets) throws PackageScanException {
        return scanner.scan(scanBase, scanTargets);
    }

    @SuppressWarnings("unchecked")
    public Map<Class, Object> generate(Set<Class> classes, Map<Class, Object> dependency) throws Exception {
        Map<Class, Object> instances = generator.generate(classes);
        instances.putAll(dependency);
        /*
        * 1. Inject instances to Configuration
        * 2. Generate Component by Configuration Method
        * 3. Inject each other
        */

        // extract configurations
        Map<Class, Object> configurations = instances.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Configuration.class))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        if (logger.isInfoEnabled()) {
            loggingConfiguration(configurations);
        }

        // handle configurations
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
            afterInject(tmp);
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
        return (T) generator.constructClass(clazz);
    }

    private void afterInject(Map<Class, Object> configurations) {
        configurations.values()
                .forEach(object -> {
                    Class<?> clazz = object.getClass();
                    Stream.of(clazz.getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(AfterInject.class))
                            .forEach(method -> {
                                logger.debug("call @AfterInject Method {} at {}", method.getName(), clazz.getName());
                                try {
                                    method.invoke(object);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new MethodInvocationException(e);
                                }
                            });
                });
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

    private void loggingDependencies(Map<Class, Object> instances) {
        StringLineBuilder sb = ChapterAttr.getBuilder("Injection Result").appendLine("ApplicationRoot");
        List<Class> sortedInstance = instances.entrySet().stream()
                .map(Map.Entry::getKey)
                .sorted((e1, e2) -> e1.getName().toLowerCase().compareTo(e2.getName().toLowerCase()))
                .collect(Collectors.toList());
        sortedInstance
                .stream()
                .limit(sortedInstance.size() - 1)
                .forEach(clazz -> showDependencies(sb, clazz, Collections.singletonList(false)));
        Class last = sortedInstance.get(sortedInstance.size() - 1);
        showDependencies(sb, last, Collections.singletonList(true));
        logger.debug(sb.toString());
    }

    private StringLineBuilder showDependencies(StringLineBuilder sb, Class clazz, List<Boolean> isClosedList) {
        String closed = "   ";
        String notClosed = "│  ";
        String last = "└─ ";
        String notLast = "├─ ";
        if (!isClosedList.isEmpty()) {
            if (isClosedList.size() > 1) {
                String row = isClosedList.subList(0, isClosedList.size() - 1).stream().map(isClosed -> isClosed ? closed : notClosed).collect(Collectors.joining());
                sb.append(row);
            }
            sb.append(isClosedList.get(isClosedList.size() - 1) ? last : notLast);
            sb.appendLine(clazz.getName());
        }
        List<Field> injectField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
        if (injectField.isEmpty()) return sb;
        else if (injectField.size() > 1) {
            ArrayList<Boolean> copy = new ArrayList<>(isClosedList);
            copy.add(false);
            injectField
                    .stream()
                    .limit(injectField.size() - 1)
                    .forEach(field -> showDependencies(sb, field.getType(), copy));
        }
        Field lastField = injectField.get(injectField.size() - 1);
        ArrayList<Boolean> copy = new ArrayList<>(isClosedList);
        copy.add(true);
        return showDependencies(sb, lastField.getType(), copy);
    }
}
