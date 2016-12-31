package org.carbon.web.handler;

import org.carbon.component.annotation.Inject;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.session.SessionContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class SessionScopeChain extends HttpScopeChain{

	private String cookieName = "CRBS";
	private static ThreadLocal<Optional<String>> tmpSessionKey = new ThreadLocal<Optional<String>>() {
		@Override
		protected Optional<String> initialValue() {
			return Optional.empty();
		}
	};

	@Inject
    private SessionContainer sessionContainer;

	@Override
	protected void in(HttpServletRequest request, HttpServletResponse response) {
		String sessionKey = Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(cookieName))
				.findFirst()
				.map(Cookie::getValue)
				.orElseGet(() -> {
					String uuid = UUID.randomUUID().toString();
					tmpSessionKey.set(Optional.of(uuid));
					return uuid;
				});
		sessionContainer.setSessionKey(sessionKey);
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
			sessionContainer.clear();
		}
	}
}
