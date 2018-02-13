package org.carbon.component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.component.enhance.ProxyEnhancer;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.IllegalDependencyException;
import org.carbon.component.exception.ImpossibleDetermineException;
import org.carbon.component.exception.MethodInvocationException;
import org.carbon.component.exception.PackageScanException;
import org.carbon.component.inject.DependencyInjector;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.carbon.component.scan.TargetBaseScanner;
import org.carbon.component.swt.SwitchQualifier;
import org.carbon.util.format.ChapterAttr;
import org.carbon.util.format.StringLineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/07.
 */
public class HandyComponentManager {
    private Logger logger = LoggerFactory.getLogger(HandyComponentManager.class);

    private TargetBaseScanner scanner = new TargetBaseScanner();
    private ProxyEnhancer enhancer = new ProxyEnhancer();
    private DependencyInjector injector = new DependencyInjector();

    public Set<Class<?>> scanComponent(Class scanBase) throws PackageScanException {
        return scanner.scan(scanBase, Collections.singleton(Component.class));
    }


    @SuppressWarnings("unchecked")
    public ComponentMetaSet resolve(ComponentMetaSet metas) {
        // add default switching qualifier
        metas.addQualifier(new SwitchQualifier());

        // construct no implementation class by enhance
        Set<Class> noImplMetaClasses = metas.stream()
                .filter(ComponentMeta::hasNoImpl)
                .map(ComponentMeta::getType)
                .collect(java.util.stream.Collectors.toSet());
        ComponentMetaSet enhancedMetas = enhancer.generate(noImplMetaClasses).entrySet().stream()
                .map(e -> ComponentMeta.implAs(e.getKey(), e.getValue()))
                .collect(ComponentMetaSet.Collectors.toSet());

        // define base component metas
        ComponentMetaSet baseMetas = metas.assign(enhancedMetas);

        // -----------------------------------------------------
        //                                               todo trying
        //                                               -------
        // try injection and resolve dependency
        ComponentMetaSet pooling = new ComponentMetaSet(baseMetas);
        int counter = 0;
        while (!pooling.isEmpty()) {
            // resolving state
            Set<ComponentMeta> unqualified = new HashSet<>();

            logger.debug("Start injection try-id[{}] only for satisfied dependency. status{remain-pool: {}, qualified: 0, unqualified: 0}", counter, pooling.size());

            ComponentMetaSet qualified = pooling.stream()
                    // try inject
                    .flatMap(meta -> {
                        try {
                            injector.inject(meta, baseMetas);
                            return Stream.of(meta);
                        } catch (ClassNotRegisteredException ignore) {
                            logger.debug("Dismiss exception[{}, {}], Retry after dependency is satisfied", ignore.getClass(), ignore.getMessage());
                            return Stream.empty();
                        }
                    })
                    // check qualification
                    .filter(meta -> {
                        try {
                            boolean isQualified = meta.isQualified();
                            if (!isQualified) {
                                logger.debug("Find unqualified meta[{}]", meta);
                                unqualified.add(meta);
                            }
                            return isQualified;
                        } catch (ImpossibleDetermineException e) {
                            logger.debug("Handle impossible determine exception {}, skip and try next", e.getMessage());
                            return false;
                        }
                    })
                    .collect(ComponentMetaSet.Collectors.toSet());

            pooling.removeAll(unqualified);
            baseMetas.removeAll(unqualified);
            pooling.removeAll(qualified);
            logger.debug("Finish injection try-id[{}] only for satisfied dependency. status{remain-pool: {}, qualified: {}, unqualified: {}}", counter++, pooling.size(), qualified.size(), unqualified.size());

            if (qualified.isEmpty() && unqualified.isEmpty()) {
                throwIllegalDependencyException(pooling);
            }

            qualified.forEach(meta -> {
                if (meta.annotatedBy(Configuration.class)) {
                    baseMetas.addAll(enhancer.generateMethodComponent(meta));
                }
                callAfterInject(meta);
            });
        }

        if (logger.isInfoEnabled()) {
            loggingConfiguration(baseMetas);
        }
        if (logger.isDebugEnabled()) {
            loggingDependencies(baseMetas);
        }

        return baseMetas;
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

    // ===================================================================================
    //                                                                          Exception
    //                                                                          ==========
    private void throwIllegalDependencyException(ComponentMetaSet configurations) {
        String message = "Fail to resolve dependency\ncheck below classes\n"
                + configurations.stream().map(meta -> meta.getType().getName()).collect(java.util.stream.Collectors.joining("\n"));
        throw new IllegalDependencyException(message);
    }

    // ===================================================================================
    //                                                                          Logging
    //                                                                          ==========
    private void loggingConfiguration(ComponentMetaSet configurationMetas) {
        String confClassNames = configurationMetas.stream()
                .filter(meta -> meta.annotatedBy(Configuration.class))
                .map(meta -> "- " + meta.getType().getName())
                .sorted(String::compareTo)
                .collect(java.util.stream.Collectors.joining("\n"));
        String mes = ChapterAttr.getBuilder("Detect Configurations class below")
                .appendLine(confClassNames).toString();
        logger.info(mes);
    }

    private void loggingDependencies(ComponentMetaSet instances) {
        StringLineBuilder sb = ChapterAttr.getBuilder("Injection Result").appendLine("ApplicationRoot");
        List<Class> sortedInstance = instances.stream()
                .map(ComponentMeta::getType)
                .sorted(Comparator.comparing(e -> e.getName().toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
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
                String row = isClosedList.stream().limit(isClosedList.size() - 1).map(isClosed -> isClosed ? closed : notClosed).collect(java.util.stream.Collectors.joining());
                sb.append(row);
            }
            sb.append(isClosedList.get(isClosedList.size() - 1) ? last : notLast);
            sb.appendLine(clazz.getName());
        }
        List<Field> injectField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(java.util.stream.Collectors.toList());
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
