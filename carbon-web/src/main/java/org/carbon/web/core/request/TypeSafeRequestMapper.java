package org.carbon.web.core.request;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shota Oda 2016/10/12.
 */
public interface TypeSafeRequestMapper {
    <T> T map(HttpServletRequest request, Class<T> mapTo);
}
