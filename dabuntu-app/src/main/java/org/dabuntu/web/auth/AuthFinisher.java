package org.dabuntu.web.auth;

import org.dabuntu.web.context.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/11/03.
 */
public interface AuthFinisher {
	void onAuth(String username, SessionContainer session);
	void onFail(HttpServletRequest request, HttpServletResponse response);
}
