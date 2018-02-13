package org.carbon.web.context.session;

import java.util.Optional;

import org.carbon.component.annotation.AfterInject;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.context.Context;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class SessionContext implements Context {
    private static ThreadLocal<String> sessionKey = ThreadLocal.withInitial(() -> "");

    @Inject(optional = true)
    private SessionStore sessionStore;

    @AfterInject
    public void afterInject() {
        if (sessionStore == null) {
            sessionStore = new InMemorySessionStore();
        }
    }

    public void setSessionKey(String sessionKey) {
        SessionContext.sessionKey.set(sessionKey);
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
            // TODO throw appropriate exception
            throw new RuntimeException("Not found SessionKey. Must set sessionKey (e.g. by use SessionScopeChain)");
        }
        return key;
    }

    public void clear() {
        sessionKey.remove();
    }
}
