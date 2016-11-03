package org.dabuntu.web.annotation;

import org.dabuntu.web.auth.AuthStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ubuntu 2016/10/27.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
	Class<? extends AuthStrategy> strategy();
}
