package org.dabuntu.web.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

	public void setObject(Object object) {
		if (!isSetSessionKey()) {
			throw new RuntimeException("Not found SessionKey. Must set sessionKey (e.g. by use SessionScopeChain)");
		}
		InstanceContainer ic = sessionKeyValue.get(sessionKey.get());

		ic.set(object);
	}

	public <T> Optional<T> getObject(Class<T> type) {
		return getSession().flatMap(session -> {
			return Optional.ofNullable(session.getInstanceByType(type));
		});
	}

	public void removeObject(Class type) {
		getSession().ifPresent(session -> session.remove(type));
	}

	public boolean existType(Class type) {
		return getSession().map(session -> session.getInstances().containsKey(type)).orElse(false);
	}

	private Optional<InstanceContainer> getSession() {
		if (!isSetSessionKey()) {
			return Optional.empty();
		}
		InstanceContainer ic = sessionKeyValue.get(sessionKey.get());

		if (ic == null) {
			InstanceContainer nic = new InstanceContainer();
			sessionKeyValue.put(sessionKey.get(), nic);
			return Optional.of(nic);
		}
		else {
			return Optional.of(ic);
		}
	}

	public void clear() {
		sessionKey.remove();
	}

	private boolean isSetSessionKey() {
		return !sessionKey.get().isEmpty();
	}
}
