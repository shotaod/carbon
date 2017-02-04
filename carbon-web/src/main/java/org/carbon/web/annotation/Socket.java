package org.carbon.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.carbon.component.annotation.Component;

/**
 * @author Shota Oda 2017/01/01.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Socket {
    String url();
}
