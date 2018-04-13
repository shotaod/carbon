package org.carbon.component.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.carbon.component.annotation.Assemble;
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

    private static final String MARKER_ASSEMBLE_LIST = "[Assemble_List  ] ";
    private static final String MARKER_ASSEMBLE_SINGULAR = "[Assemble_Single] ";

    private Logger logger = LoggerFactory.getLogger(DependencyInjector.class);

    public void inject(ComponentMeta meta, ComponentMetaSet candidates) throws ClassNotRegisteredException {
        // check switcher dependency
        Class<?> clazz = meta.getType();
        meta.awareDependency(candidates);
        for (Field field : clazz.getDeclaredFields()) {
            try {
                // assemble inject check
                Object fieldValue;
                Class<?> fieldType = field.getType();
                if (field.isAnnotationPresent(Assemble.class)) {
                    if (List.class.isAssignableFrom(fieldType)) {
                        // -----------------------------------------------------
                        //                                         assemble list
                        //                                               -------
                        Type genericType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        logger.debug(MARKER_ASSEMBLE_LIST + "target field '{}' in [{}]", field.getName(), field.getDeclaringClass().getCanonicalName());
                        if (genericType.equals(Object.class)) {
                            Assemble assembleAnnotation = field.getAnnotation(Assemble.class);
                            Set<Class<? extends Annotation>> assembleTargetAnnotations = Stream.of(assembleAnnotation.gather()).collect(java.util.stream.Collectors.toSet());
                            logger.debug(MARKER_ASSEMBLE_LIST + "search for annotation {}", assembleTargetAnnotations);
                            fieldValue = candidates.stream()
                                    .filter(entry -> assembleTargetAnnotations.stream()
                                            .anyMatch(assembleTargetAnnotation -> ((Class<?>) entry.getType()).isAnnotationPresent(assembleTargetAnnotation))
                                    )
                                    .map(ComponentMeta::getInstance)
                                    .collect(java.util.stream.Collectors.toList());
                        } else {
                            logger.debug(MARKER_ASSEMBLE_LIST + "search for type {}", genericType.getTypeName());
                            fieldValue = candidates.stream()
                                    .filter(entry -> entry.isAssignableTo(genericType))
                                    .map(ComponentMeta::getInstance)
                                    .collect(java.util.stream.Collectors.toList());
                        }
                        logger.debug(MARKER_ASSEMBLE_LIST + "result is {}", fieldValue);
                        //throwIllegalAssembleAnnotateForTypeException(clazz);
                    } else {
                        // -----------------------------------------------------
                        //                                     assemble singular
                        //                                               -------
                        logger.debug(MARKER_ASSEMBLE_SINGULAR + "search for same class [{}]", fieldType.getName());
                        ComponentMeta componentMeta = candidates.get(fieldType);
                        if (componentMeta == null) {
                            logger.debug(MARKER_ASSEMBLE_SINGULAR + "search for sub  class [{}]", fieldType.getName());
                            List<ComponentMeta> subClassCandidate = candidates.stream()
                                    .filter(subCandidateMeta -> fieldType.isAssignableFrom(subCandidateMeta.getType()))
                                    .collect(java.util.stream.Collectors.toList());
                            if (subClassCandidate.isEmpty()) {
                                Assemble injectAnnotation = field.getAnnotation(Assemble.class);
                                logger.debug(MARKER_ASSEMBLE_SINGULAR + "Fail to find dependency for field [{}] at [{}]", fieldType, field.getDeclaringClass().getName());
                                if (injectAnnotation.optional()) {
                                    logger.debug(MARKER_ASSEMBLE_SINGULAR + "Dismiss failure to find dependency, as @Assemble(optional=true)");
                                    continue;
                                }
                                throw newClassNotRegisteredException(clazz, fieldType);
                            } else if (subClassCandidate.size() > 1) {
                                throw newIllegalDependencyWithMultipleCandidateException(clazz, fieldType, subClassCandidate);
                            }
                            fieldValue = subClassCandidate.get(0).getInstance();
                        } else {
                            fieldValue = componentMeta.getInstance();
                        }
                    }
                } else continue;

                field.setAccessible(true);
                field.set(meta.getInstance(), fieldValue);
            } catch (IllegalAccessException e) {
                throw new DependencyAccessException(e);
            }
        }
    }

    private IllegalDependencyException newIllegalDependencyWithMultipleCandidateException(Class target, Class fieldClass, List<ComponentMeta> candidate) {
        return new IllegalDependencyException(
                String.format("Fail to inject to [%s] as Class[%s]\nMultiple candidate found below\n%s",
                        fieldClass,
                        target,
                        candidate.stream().map(entry -> entry.getType().getName()).collect(java.util.stream.Collectors.joining("\n"))
                )
        );
    }

    private ClassNotRegisteredException newClassNotRegisteredException(Class parent, Class child) {
        return new ClassNotRegisteredException(
                String.format(
                        "class '%s' need @Assemble '%s', but not registered",
                        parent.getName(),
                        child.getName()))
                ;
    }
}
