package org.carbon.modular.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.carbon.component.annotation.Component;

/**
 * @author Shota Oda 2017/03/06.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Property {
    String key() default "";
}
