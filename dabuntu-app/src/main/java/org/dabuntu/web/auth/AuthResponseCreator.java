package org.dabuntu.web.auth;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/11/03.
 */
public interface AuthResponseCreator {
	void create(HttpServletResponse response);
}
