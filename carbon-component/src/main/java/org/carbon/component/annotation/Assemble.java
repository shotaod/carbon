package org.carbon.component.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Inject annotation for {@link List java.util.List}.
 * This annotation is allowed for {@link List} only.
 * if no value is set, assemble for generic type,
 * if set value, assemble for specified type and then generic type must be {@link Object}
 *
 * @author Shota Oda 2017/02/26.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Assemble {
    /**
     * annotations for assemble target class
     */
    Class<? extends Annotation>[] gather() default {};

    /**
     * represent dependency is optional or not
     */
    boolean optional() default false;
}
