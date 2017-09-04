package org.carbon.component.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Inject;
import org.carbon.component.exception.ClassNotRegisteredException;
import org.carbon.component.exception.DependencyAccessException;
import org.carbon.component.exception.IllegalDependencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/01
 */
public class DependencyInjector {

    private Logger logger = LoggerFactory.getLogger(DependencyInjector.class);

    public Map<Class, Object> injectEach(Map<Class, Object> singletons) {
        Map<Class, Object> copy = new HashMap<>(singletons);
        copy.entrySet().forEach(entry -> inject(entry.getKey(), entry.getValue(), singletons));
        return copy;
    }

    public Map<Class, Object> injectOnlySatisfied(Map<Class, Object> singletons, Map<Class, Object> candidate) {
        logger.debug("Start injection only for satisfied dependency");
        return singletons.entrySet().stream()
                .flatMap(entry -> {
                    Class key = entry.getKey();
                    Object instance;
                    try {
                        instance = inject(key, entry.getValue(), candidate);
                        return Stream.of((Map.Entry<Class, Object>) new AbstractMap.SimpleEntry<>(key, instance));
                    } catch (ClassNotRegisteredException ignore) {
                        logger.debug("Dismiss exception[{}],Try again after dependency is satisfied", ignore.getClass().getCanonicalName());
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    private Object inject(Class clazz, Object object, Map<Class, Object> candidates) {

        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    try {
                        // assemble inject check
                        Object fieldValue = null;
                        Class<?> fieldType = field.getType();
                        if (field.isAnnotationPresent(Assemble.class)) {
                            Class<?> generic = (Class<?>)((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                            if (!List.class.isAssignableFrom(fieldType)) {
                                throwIllegalAssembleAnnotateException(object.getClass());
                            }

                            logger.debug("[Assemble] target field '{}' in [{}]", field.getName(), field.getDeclaringClass().getCanonicalName());
                            if (generic.equals(Object.class)) {
                                Assemble assembleAnnotation = field.getAnnotation(Assemble.class);
                                Set<Class<? extends Annotation>> assembleTargetAnnotations = Stream.of(assembleAnnotation.value()).collect(Collectors.toSet());
                                logger.debug("[Assemble] search by annotation {}", assembleTargetAnnotations);

                                fieldValue = candidates.entrySet().stream()
                                        .filter(entry -> assembleTargetAnnotations.stream()
                                                .anyMatch(assembleTargetAnnotation -> ((Class<?>)entry.getKey()).isAnnotationPresent(assembleTargetAnnotation))
                                        )
                                        .map(Map.Entry::getValue)
                                        .collect(Collectors.toList());
                            } else {
                                logger.debug("[Assemble] search by type {}", generic.getCanonicalName());
                                fieldValue = candidates.entrySet().stream()
                                        .filter(entry -> generic.isAssignableFrom(entry.getKey()))
                                        .map(Map.Entry::getValue)
                                        .collect(Collectors.toList());
                            }
                            logger.debug("[Assemble] result is {}", fieldValue);
                        } else if (field.isAnnotationPresent(Inject.class)) {
                            logger.debug("[Inject  ] search by same class [{}]", fieldType.getName());
                            fieldValue = candidates.get(fieldType);

                            if (fieldValue == null) {
                                logger.debug("[Inject  ] search by sub class of [{}]", fieldType.getName());
                                List<Map.Entry<Class, Object>> subClassCandidate = candidates.entrySet()
                                        .stream()
                                        .filter(entry -> fieldType.isAssignableFrom(entry.getKey()))
                                        .collect(Collectors.toList());
                                if (subClassCandidate.isEmpty()) {
                                    Inject injectAnnotation = field.getAnnotation(Inject.class);
                                    logger.debug("[Inject  ] Fail to find dependency for field [{}] in class [{}]", fieldType, field.getDeclaringClass().getName());
                                    if (injectAnnotation.optional()) {
                                        logger.debug("[Inject  ] Dismiss fail to find dependency, as @Inject(optional=true)");
                                        return;
                                    }
                                    throwClassNotRegisteredException(object.getClass(), fieldType);
                                } else if (subClassCandidate.size() > 1) {
                                    throwIllegalDependencyException(object.getClass(), fieldType, subClassCandidate);
                                }
                                fieldValue = subClassCandidate.get(0).getValue();
                            }
                        }

                        if (fieldValue == null) return;

                        field.setAccessible(true);
                        field.set(object, fieldValue);
                    } catch (IllegalAccessException e) {
                        throw new DependencyAccessException(e);
                    }
                });
        return object;
    }

    private void throwIllegalDependencyException(Class target, Class fieldClass, List<Map.Entry<Class, Object>> candidate) {
        throw new IllegalDependencyException(
                String.format("Fail to inject to [%s] as Class[%s]\nMultiple candidate found below\n%s",
                        fieldClass,
                        target,
                        candidate.stream().map(entry -> entry.getKey().getName()).collect(Collectors.joining("\n"))
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
