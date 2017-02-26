package org.carbon.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.carbon.web.auth.AuthStrategy;

/**
 * @author Shota Oda 2016/10/27.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
    Class<? extends AuthStrategy> strategy();
}
