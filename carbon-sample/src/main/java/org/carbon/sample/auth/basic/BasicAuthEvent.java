package org.carbon.sample.auth.basic;

import org.carbon.component.annotation.Component;
import org.carbon.web.auth.AuthEventListener;
import org.carbon.web.context.session.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/11/03.
 */
@Component
public class BasicAuthEvent implements AuthEventListener {
	private static final String Header_Auth = "WWW-Authenticate";
	@Override
	public void onAuth(String username, SessionContainer session) {

	}

	@Override
	public void onFail(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader(Header_Auth, "Basic realm=Basic");
	}
}
