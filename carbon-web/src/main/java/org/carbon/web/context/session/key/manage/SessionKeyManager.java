package org.carbon.web.context.session.key.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.web.context.session.ShouldHandleRequest;
import org.carbon.web.context.session.key.SessionKey;

/**
 * @author Shota.Oda 2018/03/04.
 */
public interface SessionKeyManager extends ShouldHandleRequest {
    SessionKey handle(HttpServletRequest request, HttpServletResponse response);
}
