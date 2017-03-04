package org.carbon.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.carbon.component.scan.TargetBaseScanner;

/**
 * This annotation prevent ClassScanner(ex. {@link TargetBaseScanner}) from scanning class.
 * @author Shota Oda 2017/03/04.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transparent {}
