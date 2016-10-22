package org.dabuntu.web.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ubuntu 2016/10/12.
 */
public class SessionContainer {
	private static ThreadLocal<String> sessionKey = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return "";
		}
	};
	private static Map<String, InstanceContainer> sessionKeyValue = new HashMap<>();

	public static SessionContainer instance = new SessionContainer();
	private SessionContainer() {}

	public void setSessionKey(String sessionKey) {
		SessionContainer.sessionKey.set(sessionKey);
	}

	public InstanceContainer getSession() {
		if (sessionKey.get().isEmpty()) {
			throw new RuntimeException("Not found SessionKey. Must set sessionKey (e.g. by use SessionScopeChain)");
		}
		InstanceContainer ic = sessionKeyValue.get(sessionKey.get());
		if (ic == null) {
			InstanceContainer container = new InstanceContainer();
			sessionKeyValue.put(sessionKey.get(), container);
			return container;
		}
		else {
			return ic;
		}
	}

	public void clear() {
		sessionKey.remove();
	}
}
