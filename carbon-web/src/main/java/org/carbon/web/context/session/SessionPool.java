package org.carbon.web.context.session;

import java.util.Optional;

import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.Pool;
import org.carbon.web.context.session.key.SessionKey;
import org.carbon.web.context.session.store.InMemorySessionStore;
import org.carbon.web.context.session.store.SessionStore;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class SessionPool implements Pool {
    private ThreadLocal<SessionKey> _threadBoundedKey = ThreadLocal.withInitial(() -> null);

    @Assemble(optional = true)
    private SessionStore sessionStore;

    @AfterAssemble
    public void afterInject() {
        if (sessionStore == null) {
            sessionStore = new InMemorySessionStore();
        }
    }

    public void start(SessionKey sessionKey) {
        _threadBoundedKey.set(sessionKey);
    }

    public void end() {
        _threadBoundedKey.remove();
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

    private SessionKey getSessionKey() {
        SessionKey key = _threadBoundedKey.get();
        if (key == null) {
            throw new IllegalStateException("Not found SessionKey. Start session by DelegateSessionManager etc");
        }
        return key;
    }
}
