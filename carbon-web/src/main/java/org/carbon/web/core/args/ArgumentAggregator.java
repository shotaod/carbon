package org.carbon.web.core.args;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;

import org.carbon.web.container.ArgumentMeta;

/**
 * @author Shota.Oda 2018/02/22.
 */
public interface ArgumentAggregator<A extends Annotation> {

    Class<A> target();

    default boolean handle(Parameter parameter) {
        return parameter.isAnnotationPresent(target());
    }

    ArgumentMeta aggregate(Parameter parameter, HttpServletRequest request);
}
