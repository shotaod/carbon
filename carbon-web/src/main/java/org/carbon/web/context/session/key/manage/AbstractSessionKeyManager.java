package org.carbon.web.context.session.key.manage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.carbon.web.context.session.ShouldHandleRequest;
import org.carbon.web.context.session.key.SessionKey;
import org.carbon.web.context.session.key.SessionKeyGenerator;

/**
 * @author Shota.Oda 2018/03/04.
 */
public abstract class AbstractSessionKeyManager implements SessionKeyManager {

    protected ShouldHandleRequest shouldHandle;
    protected Map<String, SessionKey> keyHolder;
    protected SessionKeyGenerator keyGenerator;

    public AbstractSessionKeyManager(
            ShouldHandleRequest shouldHandle,
            SessionKeyGenerator keyGenerator) {
        this.shouldHandle = shouldHandle;
        this.keyHolder = new HashMap<>();
        this.keyGenerator = keyGenerator;
    }

    @Override
    public boolean should(HttpServletRequest request) {
        return shouldHandle.should(request);
    }

    // -----------------------------------------------------
    //                                               Key Management
    //                                               -------
    protected SessionKey generateKey() {
        SessionKey key = keyGenerator.generate();
        addKey(key);
        return key;
    }

    private void addKey(SessionKey key) {
        keyHolder.put(key.key(), key);
    }

    protected SessionKey getKey(String key) {
        removeExpiredKey();
        return keyHolder.get(key);
    }

    private void removeExpiredKey() {
        keyHolder.entrySet().stream()
                .filter(e -> e.getValue().expired())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .forEach(keyHolder::remove);
    }
}
