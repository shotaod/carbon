package org.dabuntu.sample.auth.consumer;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.sample.auth.form.FormAuthEvent;
import org.dabuntu.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/11/23.
 */
@Component
public class ConsumerAuthEvent extends FormAuthEvent {
	@Override
	public void onFail(HttpServletRequest request, HttpServletResponse response) {
		ResponseUtil.redirect(response, "/consumer/login");
	}
}
