package org.dabuntu.sample.auth.basic;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.auth.AuthFinisher;
import org.dabuntu.web.context.SessionContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/11/03.
 */
@Component
public class BasicAuthFinisher implements AuthFinisher {
	@Override
	public void onAuth(String username, SessionContainer session) {

	}

	@Override
	public void onFail(HttpServletRequest request, HttpServletResponse response) {

	}
}
