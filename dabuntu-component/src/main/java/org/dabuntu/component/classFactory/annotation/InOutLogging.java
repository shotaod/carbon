package org.dabuntu.component.classFactory.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;

/**
 * @author ubuntu 2016/10/02.
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface InOutLogging {}
