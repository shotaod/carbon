package org.carbon.web.translate.decorate;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota.Oda 2018/02/18.
 */
public interface HttpDecorator {
    void decorate(HttpServletResponse response);
}
