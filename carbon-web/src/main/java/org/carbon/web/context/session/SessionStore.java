package org.carbon.web.context.session;

/**
 * @author ubuntu 2016/12/17.
 */
public interface SessionStore {
    <T> T get(String key, Class<T> type);
    void put(String key, Object object);
    void remove(String key, Class type);
}
