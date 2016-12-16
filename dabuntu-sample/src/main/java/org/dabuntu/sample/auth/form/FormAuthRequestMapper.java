package org.dabuntu.sample.auth.form;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.auth.AuthRequestMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author ubuntu 2016/11/04.
 */
@Component
public class FormAuthRequestMapper implements AuthRequestMapper{

	@Override
	public Optional<AuthInfo> map(HttpServletRequest request) {
		Object usernameObj = request.getParameter("username");
		Object passwordObj = request.getParameter("password");
		String username = Optional.ofNullable(usernameObj).map(Object::toString).orElse("");
		String password = Optional.ofNullable(passwordObj).map(Object::toString).orElse("");

		return Optional.of(new AuthInfo(username, password));
	}
}
