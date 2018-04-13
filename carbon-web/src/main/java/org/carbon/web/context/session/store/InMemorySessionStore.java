package org.carbon.web.context.session.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.carbon.web.context.InstanceContainer;
import org.carbon.web.context.session.key.SessionKey;

/**
 * @author Shota Oda 2016/12/17.
 */
public class InMemorySessionStore implements SessionStore {
    /**
     * Wrapper class for sessionKey to use as Map-Key implementing equals and hash code
     */
    private static class EHSessionKey {
        private SessionKey sessionKey;

        public EHSessionKey(SessionKey sessionKey) {
            this.sessionKey = sessionKey;
        }

        public String key() {
            return sessionKey.key();
        }

        public Boolean expired() {
            return sessionKey.expired();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EHSessionKey)) return false;

            EHSessionKey that = (EHSessionKey) o;
            return sessionKey.key().equals(that.sessionKey.key());
        }

        @Override
        public int hashCode() {
            return sessionKey.key().hashCode();
        }
    }

    private Map<EHSessionKey, InstanceContainer> store;

    public InMemorySessionStore() {
        store = new HashMap<>();
    }

    @Override
    public <T> T get(SessionKey key, Class<T> type) {
        return Optional.ofNullable(store.get(new EHSessionKey(key)))
                .map(ic -> ic.getByType(type))
                .orElse(null);
    }

    @Override
    public void put(SessionKey key, Object object) {
        // remove expired
        List<EHSessionKey> expiredKeys = store.keySet().stream()
                .filter(EHSessionKey::expired)
                .collect(Collectors.toList());
        expiredKeys.forEach(store::remove);

        InstanceContainer ic = store.computeIfAbsent(new EHSessionKey(key), k -> new InstanceContainer());
        ic.set(object);
    }

    @Override
    public void remove(SessionKey key, Class type) {
        Optional.ofNullable(store.get(new EHSessionKey(key)))
                .ifPresent(ic -> ic.remove(type));
    }
}
