package org.carbon.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shota.Oda 2018/02/23.
 */
public class InheritanceAnnotation<A extends Annotation> {
    private static final Set<Class<? extends Annotation>> RESERVED_ANNOTATION = Stream.of(
            Documented.class,
            Retention.class,
            Target.class
    ).collect(Collectors.toSet());

    private Class<A> type;

    public InheritanceAnnotation(Class<A> type) {
        this.type = type;
    }

    public boolean isAnnotated(Class<? extends Annotation> superType) {
        return isAssignableTo(type, superType, new HashSet<>());
    }

    private boolean isAssignableTo(Class<? extends Annotation> base, Class<? extends Annotation> target, Set<Class<? extends Annotation>> history) {
        if (history.contains(base)) {
            return false;
        }
        history.add(base);
        if (base.equals(target)) {
            return true;
        }
        Annotation[] declaredAnnotations = base.getDeclaredAnnotations();
        return declaredAnnotations.length != 0
                && Stream.of(declaredAnnotations)
                .map(Annotation::annotationType)
                .filter(annotationType -> !RESERVED_ANNOTATION.contains(annotationType) && !history.contains(annotationType))
                .anyMatch(annotationType -> isAssignableTo(annotationType, target, history));
    }
}
