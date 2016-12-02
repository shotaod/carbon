package org.dabuntu.component.generator.aop.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ubuntu 2016/10/02.
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface InOutLogging {}
