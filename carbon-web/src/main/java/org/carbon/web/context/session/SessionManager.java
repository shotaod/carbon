package org.carbon.web.context.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota.Oda 2018/03/03.
 */
public interface SessionManager {
    void start(HttpServletRequest request, HttpServletResponse response);

    void end();
}
