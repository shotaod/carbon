package org.carbon.authentication.strategy;

import org.carbon.authentication.AuthIdentity;
import org.carbon.web.context.session.SessionContext;

/**
 * @author garden 2018/02/12.
 */
public abstract class AbstractSessionAuthStrategy<IDENTITY extends AuthIdentity> implements AuthStrategy {
    protected Class<IDENTITY> identityClass;
    protected SessionContext sessionContext;

    public AbstractSessionAuthStrategy(Class<IDENTITY> identityClass, SessionContext sessionContext) {
        this.identityClass = identityClass;
        this.sessionContext = sessionContext;
    }

    @Override
    public boolean existSession() {
        return sessionContext.getByType(identityClass).isPresent();
    }

    @Override
    public void onExpire() {
        sessionContext.removeObject(identityClass);
    }

    @Override
    public void onAuth(AuthIdentity identity) {
        sessionContext.setObject(identity);
    }
}
