package org.carbon.web.context.session;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shota.Oda 2018/03/04.
 */
@FunctionalInterface
public interface ShouldHandleRequest /*extends Function<HttpServletRequest, Boolean>*/ {
    boolean should(HttpServletRequest request);
}