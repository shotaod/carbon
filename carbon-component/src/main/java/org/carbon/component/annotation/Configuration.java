package org.carbon.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class annotated by @Configuration can produce Component by Method.<br/>
 * Target method should be annotated by {@link Component}.<br/>
 * Class annotated by @Configuration is also resolved @Inject like {@link Component}.<br/>
 * @see Component
 * @author Shota Oda 2016/10/15.
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {
}
