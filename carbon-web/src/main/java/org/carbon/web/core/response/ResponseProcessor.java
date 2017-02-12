package org.carbon.web.core.response;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/10/14.
 */
public interface ResponseProcessor {
    boolean process (HttpServletResponse response);
}
