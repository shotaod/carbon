package org.carbon.web.context.session;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;

import java.util.Optional;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class SessionContainer {
    private SessionContainer() {}
    private static ThreadLocal<String> sessionKey = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "";
        }
    };
    @Inject
    private SessionStore sessionStore = new InMemorySessionStore();

    public void setSessionKey(String sessionKey) {
        SessionContainer.sessionKey.set(sessionKey);
    }

    public void setObject(Object object) {
        sessionStore.put(getSessionKey(), object);
    }

    public <T> Optional<T> getByType(Class<T> type) {
        return Optional.ofNullable(sessionStore.get(getSessionKey(), type));
    }

    public void removeObject(Class type) {
        sessionStore.remove(getSessionKey(), type);
    }

    private String getSessionKey() {
        String key = sessionKey.get();
        if (key == null || key.isEmpty()) {
            throw new RuntimeException("Not found SessionKey. Must set sessionKey (e.g. by use SessionScopeChain)");
        }
        return key;
    }

    public void clear() {
        sessionKey.remove();
    }

    private boolean isSetSessionKey() {
        return !sessionKey.get().isEmpty();
    }
}
