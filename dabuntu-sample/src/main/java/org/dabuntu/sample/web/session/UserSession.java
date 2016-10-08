package org.dabuntu.sample.web.session;

import org.dabuntu.component.instanceFactory.annotation.Component;

/**
 * @author ubuntu 2016/10/01
 */
@Component
public class UserSession {
	public Long userId;

	public Long getUserId() {
		return userId;
	}

	public UserSession() {
		this.userId = 1L;
	}
}
