package org.dabuntu.sample.auth.basic;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.auth.AuthResponseCreator;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class BasicAuthResponseCreator implements AuthResponseCreator {
	private static final String Header_Auth = "WWW-Authenticate";
	@Override
	public void create(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader(Header_Auth, "Basic realm=Basic");
	}
}
