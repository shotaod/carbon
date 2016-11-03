package org.dabuntu.web.auth;

import org.dabuntu.web.context.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/28.
 */
public interface AuthFunction {
	boolean apply (HttpServletRequest request, HttpServletResponse response, SessionContainer session);
}
