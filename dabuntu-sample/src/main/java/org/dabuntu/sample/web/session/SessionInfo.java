package org.dabuntu.sample.web.session;

import org.dabuntu.component.annotation.Component;

/**
 * @author ubuntu 2016/10/01
 */
@Component
public class SessionInfo {
	public Long userId;

	public Long getUserId() {
		return userId;
	}

	public SessionInfo() {
		this.userId = 1L;
	}
}
