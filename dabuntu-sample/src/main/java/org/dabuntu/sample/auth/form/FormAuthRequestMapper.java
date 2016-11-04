package org.dabuntu.sample.auth.form;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.auth.AuthRequestMapper;
import org.dabuntu.web.container.request.MappedRequestBody;
import org.dabuntu.web.core.RequestParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author ubuntu 2016/11/04.
 */
@Component
public class FormAuthRequestMapper implements AuthRequestMapper{

	@Inject
	private RequestParser requestParser;

	@Override
	public Optional<AuthInfo> map(HttpServletRequest request) {
		MappedRequestBody requestBody = requestParser.parse(request).getMappedRequestBody();
		Object usernameObj = requestBody.getValue("username");
		Object passwordObj = requestBody.getValue("password");
		String username = Optional.ofNullable(usernameObj).map(Object::toString).orElse("");
		String password = Optional.ofNullable(passwordObj).map(Object::toString).orElse("");

		return Optional.of(new AuthInfo(username, password));
	}
}
