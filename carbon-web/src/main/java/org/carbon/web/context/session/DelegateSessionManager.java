package org.carbon.web.context.session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.session.key.SessionKey;
import org.carbon.web.context.session.key.TTLSessionKey;
import org.carbon.web.context.session.key.manage.CookieSessionKeyManager;
import org.carbon.web.context.session.key.manage.SessionKeyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota.Oda 2018/03/03.
 */
@Component
public class DelegateSessionManager implements SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(DelegateSessionManager.class);
    private static final String DEFAULT_KEY = "CRBNS";

    @Assemble
    private SessionPool sessionPool;

    // @Assemble ...mmm
    // In carbon, Component cannot assemble List of Configuration Component perfectly
    // in terms of invocation order
    private List<SessionKeyManager> keyMangers;
    private AtomicBoolean started = new AtomicBoolean(false);

    public DelegateSessionManager() {
        this.keyMangers = new ArrayList<>();
    }

    public void addKeyManager(SessionKeyManager sessionKeyManager) {
        if (started.get()) {
            throw new IllegalStateException("cannot add key manager once started");
        }

        keyMangers.add(sessionKeyManager);
    }

    @Override
    public void start(HttpServletRequest request, HttpServletResponse response) {
        if (started.compareAndSet(false, true)) {
            logger.debug("[session] manager started");
            // setup default
            if (keyMangers.isEmpty()) {
                logger.info("[session] No session key manger found, create default(CookieSessionKeyManager)");
                keyMangers.add(new CookieSessionKeyManager(
                        req -> true,
                        this::defaultKey,
                        DEFAULT_KEY
                ));
            }
        }

        for (SessionKeyManager keyManger : keyMangers) {
            if (keyManger.should(request)) {
                logger.debug("[session] manager found, start session");
                SessionKey key = keyManger.handle(request, response);
                sessionPool.start(key);
                return;
            }
        }

        logger.debug("[session] no manager found, no session available");
    }

    @Override
    public void end() {
        sessionPool.end();
    }

    private SessionKey defaultKey() {
        return new TTLSessionKey(UUID.randomUUID().toString().replace("-", ""));
    }
}
