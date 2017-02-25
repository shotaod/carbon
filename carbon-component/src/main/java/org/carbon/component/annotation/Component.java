package org.carbon.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>At Type</h1>
 * Produce Component that is resolved @Inject Field
 * <br>
 * <h1>At Method</h1>
 * (using with @Configuration annotation)
 * Produce Component by method
 * <p>but, currently supported pure generation, means that Method process must not have dependencies</p>
 * @author Shota Oda 2016/10/02
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {}