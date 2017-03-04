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
 * if no value is set, assemble List generic type
 * else, assemble by set annotations then.
 * if set value, List generic type must be {@link Object}
 * @author Shota Oda 2017/02/26.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Assemble {
    /**
     * annotations for assemble target class
     */
    Class<? extends Annotation>[] value() default {};
}
