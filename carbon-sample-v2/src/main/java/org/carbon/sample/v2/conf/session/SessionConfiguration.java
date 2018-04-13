package org.carbon.sample.v2.conf.session;

import org.apache.commons.text.RandomStringGenerator;
import org.carbon.component.annotation.AfterAssemble;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Configuration;
import org.carbon.web.context.session.DelegateSessionManager;
import org.carbon.web.context.session.key.TTLSessionKey;
import org.carbon.web.context.session.key.manage.CookieSessionKeyManager;
import org.carbon.web.context.session.key.manage.HeaderSessionKeyManager;
import org.carbon.web.context.session.key.manage.SessionKeyManager;

/**
 * @author Shota.Oda 2018/03/20.
 */
@Configuration
public class SessionConfiguration {

    private static final String COOKIE_KEY = "CRBNSID";
    private static final String HEADER_KEY = "X-ROCKETTY-SESSION";

    @Assemble
    private DelegateSessionManager sessionManager;
    @Assemble
    private RandomStringGenerator randomStringGenerator;

    @AfterAssemble
    public void afterAssemble() {
        SessionKeyManager defaultKeyManager = new CookieSessionKeyManager(
                req -> !req.getRequestURI().startsWith("/api/v1/rocketty"),
                () -> new TTLSessionKey(randomStringGenerator.generate(16)),
                COOKIE_KEY);
        HeaderSessionKeyManager rockettyKeyManger = new HeaderSessionKeyManager(
                req -> req.getRequestURI().startsWith("/api/v1/rocketty"),
                () -> new TTLSessionKey(randomStringGenerator.generate(16)),
                HEADER_KEY
        );
        sessionManager.addKeyManager(defaultKeyManager);
        sessionManager.addKeyManager(rockettyKeyManger);
    }
}
