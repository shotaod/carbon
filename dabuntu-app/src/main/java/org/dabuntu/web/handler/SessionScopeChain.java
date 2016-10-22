package org.dabuntu.web.handler;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.context.InstanceContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class SessionScopeChain extends HttpScopeChain{

	private String cookieName = "DBTSESSIONID";
	private static ThreadLocal<Optional<String>> tmpSessionKey = new ThreadLocal<Optional<String>>() {
		@Override
		protected Optional<String> initialValue() {
			return Optional.empty();
		}
	};

	@Override
	protected void in(HttpServletRequest request, HttpServletResponse response) {
		String sessionKey = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(cookieName))
				.findFirst()
				.map(cookie -> cookie.getValue())
				.orElseGet(() -> {
					String uuid = UUID.randomUUID().toString();
					tmpSessionKey.set(Optional.of(uuid));
					return uuid;
				});
		ApplicationPool.instance.getSessionPool().setSessionKey(sessionKey);
	}

	@Override
	protected void out(HttpServletRequest request, HttpServletResponse response) {
		try {
			tmpSessionKey.get().ifPresent(sessionKey -> {
				Cookie cookie = new Cookie(this.cookieName, sessionKey);
				cookie.setHttpOnly(true);
				cookie.setPath("/");
				response.addCookie(cookie);
			});
		} finally {
			tmpSessionKey.remove();
			ApplicationPool.instance.getSessionPool().clear();
		}
	}
}
