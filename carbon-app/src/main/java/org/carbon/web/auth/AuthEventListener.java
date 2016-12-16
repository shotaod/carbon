package org.carbon.web.auth;

import org.carbon.web.context.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/11/03.
 */
public interface AuthEventListener {
	void onAuth(String username, SessionContainer session);
	void onFail(HttpServletRequest request, HttpServletResponse response);
}
