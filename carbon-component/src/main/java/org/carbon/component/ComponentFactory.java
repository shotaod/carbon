package org.carbon.component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.enhance.ProxyEnhancer;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.IllegalDependencyException;
import org.carbon.component.exception.MethodInvocationException;
import org.carbon.component.exception.PackageScanException;
import org.carbon.component.inject.DependencyInjector;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.carbon.component.scan.TargetBaseScanner;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.format.StringLineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/07.
 */
public class ComponentFactory {
    private Logger logger = LoggerFactory.getLogger(ComponentFactory.class);

    private TargetBaseScanner scanner = new TargetBaseScanner();
    private ProxyEnhancer enhancer = new ProxyEnhancer();
    private DependencyInjector injector = new DependencyInjector();

    public Set<Class<?>> scanComponent(Class scanBase) throws PackageScanException {
        return scanner.scan(scanBase, Collections.singleton(Component.class));
    }


    @SuppressWarnings("unchecked")
    public ComponentMetaSet resolve(ComponentMetaSet metas) {
        Set<Class> noImplMetaClasses = metas.stream()
                .filter(ComponentMeta::hasNoImpl)
                .map(ComponentMeta::getType)
                .collect(Collectors.toSet());
        ComponentMetaSet enhancedMetas = enhancer.generate(noImplMetaClasses).entrySet().stream()
                .map(e -> ComponentMeta.implAs(e.getKey(), e.getValue()))
                .collect(ComponentMetaSet.MetaCollectors.toSet());
        ComponentMetaSet baseMetas = metas.assign(enhancedMetas);

        /*
         * 1. Inject instances to Configuration
         * 2. Generate Component by Configuration Method
         * 3. Inject each other
         */
        // extract configurations
        ComponentMetaSet configurationMetas = baseMetas.stream()
                .filter(meta -> meta.getType().isAnnotationPresent(Configuration.class))
                .collect(ComponentMetaSet.MetaCollectors.toSet());

        if (logger.isInfoEnabled()) {
            loggingConfiguration(configurationMetas);
        }

        // handle configurations
        while (!configurationMetas.isEmpty()) {
            // check switching configuration
            ComponentMetaSet switchOffMetas = configurationMetas.stream().filter(meta -> {
                try {
                    return !meta.isQualified(baseMetas);
                } catch (ClassNotRegisteredException e) {
                    logger.debug("Dismiss exception. ({}, {}) Wait for being loaded Switcher", e.getClass(), e.getMessage());
                    return false;
                }
            }).collect(ComponentMetaSet.MetaCollectors.toSet());
            configurationMetas.removeAll(switchOffMetas);

            // try inject
            ComponentMetaSet tmp = injector.injectOnlySatisfied(configurationMetas, baseMetas).stream()
                    .peek(configurationMetas::remove)
                    .collect(ComponentMetaSet.MetaCollectors.toSet());

            // Regard as dependency exception,
            // caz No meta element is resolved in this loop (implying next loop also resolve no meta)
            if (tmp.isEmpty()) {
                throwIllegalDependencyException(configurationMetas);
            }

            tmp.forEach(this::callAfterInject);
            baseMetas.addAll(enhancer.generateMethodComponent(tmp));
        }

        // check switching component
        ComponentMetaSet qualifiedMetas = baseMetas.stream()
                .filter(meta -> meta.isQualified(baseMetas))
                .collect(ComponentMetaSet.MetaCollectors.toSet());
        ComponentMetaSet injected = injector.injectEach(qualifiedMetas);
        if (logger.isDebugEnabled()) {
            loggingDependencies(injected);
        }
        return injected.stream()
                // call after inject without configuration
                .peek(meta -> {
                    if (!meta.getType().isAnnotationPresent(Configuration.class)) {
                        callAfterInject(meta);
                    }
                })
                .collect(ComponentMetaSet.MetaCollectors.toSet());
    }

    private void callAfterInject(ComponentMeta meta) {
        Stream.of(meta.getType().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AfterInject.class))
                .forEach(method -> {
                    logger.debug("call @AfterInject Method {} at {}", method.getName(), meta.getType());
                    try {
                        method.invoke(meta.getInstance());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new MethodInvocationException(e);
                    }
                });
    }

    private void throwIllegalDependencyException(ComponentMetaSet configurations) {
        String message = "Fail to resolve dependency\ncheck below classes\n"
                + configurations.stream().map(meta -> meta.getType().getName()).collect(Collectors.joining("\n"));
        throw new IllegalDependencyException(message);
    }

    // ===================================================================================
    //                                                                          Logging
    //                                                                          ==========
    private void loggingConfiguration(ComponentMetaSet configurationMetas) {
        String confClassNames = configurationMetas.stream().map(meta -> "- " + meta.getType().getName()).sorted(String::compareTo).collect(Collectors.joining("\n"));
        String mes = ChapterAttr.getBuilder("Detect Configurations class below")
                .appendLine(confClassNames).toString();
        logger.info(mes);
    }

    private void loggingDependencies(ComponentMetaSet instances) {
        StringLineBuilder sb = ChapterAttr.getBuilder("Injection Result").appendLine("ApplicationRoot");
        List<Class> sortedInstance = instances.stream()
                .map(ComponentMeta::getType)
                .sorted(Comparator.comparing(e -> e.getName().toLowerCase()))
                .collect(Collectors.toList());
        sortedInstance
                .stream()
                .limit(sortedInstance.size() - 1)
                .forEach(clazz -> getDependencyTree(sb, clazz, Collections.singletonList(false)));
        Class last = sortedInstance.get(sortedInstance.size() - 1);
        getDependencyTree(sb, last, Collections.singletonList(true));
        logger.debug(sb.toString());
    }

    private StringLineBuilder getDependencyTree(StringLineBuilder sb, Class clazz, List<Boolean> isClosedList) {
        String closed = "   ";
        String notClosed = "│  ";
        String last = "└─ ";
        String notLast = "├─ ";
        if (!isClosedList.isEmpty()) {
            if (isClosedList.size() > 1) {
                String row = isClosedList.stream().limit(isClosedList.size() - 1).map(isClosed -> isClosed ? closed : notClosed).collect(Collectors.joining());
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
                    .forEach(field -> getDependencyTree(sb, field.getType(), copy));
        }
        Field lastField = injectField.get(injectField.size() - 1);
        ArrayList<Boolean> copy = new ArrayList<>(isClosedList);
        copy.add(true);
        return getDependencyTree(sb, lastField.getType(), copy);
    }
}
