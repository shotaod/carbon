package org.carbon.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Shota.Oda 2018/02/23.
 */
public class AnnotationUtil {

    public static boolean isAnnotated(Class<?> target, Class<? extends Annotation> annotator) {
        return isAnnotated(target, Collections.singleton(annotator));
    }

    public static boolean isAnnotated(Class<?> target, Set<Class<? extends Annotation>> annotators) {
        return isAnnotated(target.getDeclaredAnnotations(), annotators);
    }

    public static boolean isAnnotated(Parameter parameter, Class<? extends Annotation> annotator) {
        return isAnnotated(parameter, Collections.singleton(annotator));
    }

    public static boolean isAnnotated(Parameter parameter, Set<Class<? extends Annotation>> annotators) {
        return isAnnotated(parameter.getDeclaredAnnotations(), annotators);
    }

    private static boolean isAnnotated(Annotation[] annotations, Set<Class<? extends Annotation>> annotators) {
        return Stream.of(annotations)
                .map(Annotation::annotationType)
                .map(InheritanceAnnotation::new)
                .anyMatch(inheritanceAnnotation -> annotators.stream().anyMatch(inheritanceAnnotation::isAnnotated));
    }
}
