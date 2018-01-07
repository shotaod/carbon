package org.carbon.component.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.Switcher;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Inject;
import org.carbon.component.annotation.Switch;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.DependencyAccessException;
import org.carbon.component.exception.IllegalDependencyException;
import org.carbon.component.meta.ComponentMeta;
import org.carbon.component.meta.ComponentMetaSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/01
 */
public class DependencyInjector {

    private Logger logger = LoggerFactory.getLogger(DependencyInjector.class);

    public ComponentMetaSet injectEach(ComponentMetaSet singletons) {
        ComponentMetaSet copy = new ComponentMetaSet(singletons);
        copy.forEach(meta -> inject(meta, singletons));
        return copy;
    }

    public ComponentMetaSet injectOnlySatisfied(ComponentMetaSet targets, ComponentMetaSet candidate) {
        logger.debug("Start injection only for satisfied dependency");
        return targets.stream()
                .flatMap(meta -> {
                    try {
                        inject(meta, candidate);
                        return Stream.of(meta);
                    } catch (ClassNotRegisteredException ignore) {
                        logger.debug("Dismiss exception[{}],Try again after dependency is satisfied", ignore.getClass().getCanonicalName());
                        return Stream.empty();
                    }
                })
                .collect(ComponentMetaSet.MetaCollectors.toSet());
    }

    private void inject(ComponentMeta meta, ComponentMetaSet candidates) {
        // check switcher dependency
        Class<?> clazz = meta.getType();
        Switch switchAnnotation = clazz.getDeclaredAnnotation(Switch.class);
        if (switchAnnotation != null) {
            Class<? extends Switcher> switcherClass = switchAnnotation.value();
            ComponentMeta<? extends Switcher> switcherMeta = candidates.get(switcherClass);
            if (switcherMeta == null) {
                throwClassNotRegisteredException(clazz, switcherClass);
            }
        }
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    try {
                        // assemble inject check
                        Object fieldValue = null;
                        Class<?> fieldType = field.getType();
                        if (field.isAnnotationPresent(Assemble.class)) {
                            Class<?> generic = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                            if (!List.class.isAssignableFrom(fieldType)) {
                                throwIllegalAssembleAnnotateException(clazz);
                            }

                            logger.debug("[Assemble] target field '{}' in [{}]", field.getName(), field.getDeclaringClass().getCanonicalName());
                            if (generic.equals(Object.class)) {
                                Assemble assembleAnnotation = field.getAnnotation(Assemble.class);
                                Set<Class<? extends Annotation>> assembleTargetAnnotations = Stream.of(assembleAnnotation.value()).collect(Collectors.toSet());
                                logger.debug("[Assemble] search for annotation {}", assembleTargetAnnotations);
                                fieldValue = candidates.stream()
                                        .filter(entry -> assembleTargetAnnotations.stream()
                                                .anyMatch(assembleTargetAnnotation -> ((Class<?>) entry.getType()).isAnnotationPresent(assembleTargetAnnotation))
                                        )
                                        .map(ComponentMeta::getInstance)
                                        .collect(Collectors.toList());
                            } else {
                                logger.debug("[Assemble] search for type {}", generic.getCanonicalName());
                                fieldValue = candidates.stream()
                                        .filter(entry -> generic.isAssignableFrom(entry.getType()))
                                        .map(ComponentMeta::getInstance)
                                        .collect(Collectors.toList());
                            }
                            logger.debug("[Assemble] result is {}", fieldValue);
                        } else if (field.isAnnotationPresent(Inject.class)) {
                            logger.debug("[Inject  ] search for same class [{}]", fieldType.getName());
                            ComponentMeta componentMeta = candidates.get(fieldType);
                            if (componentMeta == null) {
                                logger.debug("[Inject  ] search for sub class of [{}]", fieldType.getName());
                                List<ComponentMeta> subClassCandidate = candidates.stream()
                                        .filter(subCandidateMeta -> fieldType.isAssignableFrom(subCandidateMeta.getType()))
                                        .collect(Collectors.toList());
                                if (subClassCandidate.isEmpty()) {
                                    Inject injectAnnotation = field.getAnnotation(Inject.class);
                                    logger.debug("[Inject  ] Fail to find dependency for field [{}] in class [{}]", fieldType, field.getDeclaringClass().getName());
                                    if (injectAnnotation.optional()) {
                                        logger.debug("[Inject  ] Dismiss fail to find dependency, as @Inject(optional=true)");
                                        return;
                                    }
                                    throwClassNotRegisteredException(clazz, fieldType);
                                } else if (subClassCandidate.size() > 1) {
                                    throwIllegalDependencyException(clazz, fieldType, subClassCandidate);
                                }
                                fieldValue = subClassCandidate.get(0).getInstance();
                            } else {
                                fieldValue = componentMeta.getInstance();
                            }
                        }

                        if (fieldValue == null) return;

                        field.setAccessible(true);
                        field.set(meta.getInstance(), fieldValue);
                    } catch (IllegalAccessException e) {
                        throw new DependencyAccessException(e);
                    }
                });
    }

    private void throwIllegalDependencyException(Class target, Class fieldClass, List<ComponentMeta> candidate) {
        throw new IllegalDependencyException(
                String.format("Fail to inject to [%s] as Class[%s]\nMultiple candidate found below\n%s",
                        fieldClass,
                        target,
                        candidate.stream().map(entry -> entry.getType().getName()).collect(Collectors.joining("\n"))
                )
        );
    }

    private void throwClassNotRegisteredException(Class parent, Class child) {
        throw new ClassNotRegisteredException(
                String.format(
                        "class '%s' need @Inject '%s', but not registered",
                        parent.getName(),
                        child.getName()))
                ;
    }

    private void throwIllegalAssembleAnnotateException(Class clazz) {
        throw new IllegalDependencyException(
                String.format("Detect illegal annotation at %s.\n @Assemble is allowed only to 'List<Object>'",
                        clazz.getName())
        );
    }
}
