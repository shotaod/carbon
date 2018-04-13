package org.carbon.web.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota.Oda 2018/02/16.
 */
public interface HttpTranslator<PAYLOAD> {
    default boolean supported(Object object) {
        return targetType().isAssignableFrom(object.getClass());
    }

    Class<PAYLOAD> targetType();

    void translate(HttpServletRequest request, HttpServletResponse response, PAYLOAD payload) throws Throwable;
}
