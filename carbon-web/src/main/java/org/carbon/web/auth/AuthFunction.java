package org.carbon.web.auth;

import org.carbon.web.context.session.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/10/28.
 */
public interface AuthFunction {
	boolean apply (HttpServletRequest request, HttpServletResponse response, SessionContainer session);
}
