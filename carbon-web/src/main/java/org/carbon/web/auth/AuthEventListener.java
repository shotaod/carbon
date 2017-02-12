package org.carbon.web.auth;

import org.carbon.web.context.session.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthEventListener {
    void onAuth(String username, SessionContainer sessionContainer);
    void onFail(HttpServletRequest request, HttpServletResponse response);
}
