package org.carbon.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for register component.
 * This annotation can be on Class or Method, and behave differently.
 * <h1>At Type</h1>
 * Produce Component Class itself that is injected to other Class(annotated by @Component) field annotated by {@link Inject}
 * <br />
 * <h1>At Method</h1>
 * (using with {@link Configuration} annotation)
 * Produce Component by invoking annotated method
 * @author Shota Oda 2016/10/02
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {}
