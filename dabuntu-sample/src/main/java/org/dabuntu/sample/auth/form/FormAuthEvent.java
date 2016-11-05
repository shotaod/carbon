package org.dabuntu.sample.auth.form;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.sample.web.session.SessionInfo;
import org.dabuntu.web.auth.AuthEventListener;
import org.dabuntu.web.context.SessionContainer;
import org.dabuntu.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class FormAuthEvent implements AuthEventListener {
	@Override
	public void onAuth(String username, SessionContainer session) {
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setUsername(username);
		sessionInfo.setDateTime(LocalDateTime.now());
		session.setObject(sessionInfo);
	}

	@Override
	public void onFail(HttpServletRequest request, HttpServletResponse response) {
		ResponseUtil.redirect(response, "/form/login");
	}
}
