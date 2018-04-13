package org.carbon.web.context.session.store;

import org.carbon.web.context.session.key.SessionKey;

/**
 * @author Shota Oda 2016/12/17.
 */
public interface SessionStore {
    <T> T get(SessionKey key, Class<T> type);

    void put(SessionKey key, Object object);

    void remove(SessionKey key, Class type);
}
