package org.carbon.authentication.strategy;

import org.carbon.authentication.AuthIdentity;
import org.carbon.authentication.translator.SignedTranslatable;
import org.carbon.web.context.session.SessionPool;

/**
 * @author Shota.Oda 2018/02/12.
 */
public abstract class AbstractSessionAuthStrategy<IDENTITY extends AuthIdentity> implements AuthStrategy {
    protected Class<IDENTITY> identityClass;
    protected SessionPool sessionContext;

    public AbstractSessionAuthStrategy(Class<IDENTITY> identityClass, SessionPool sessionContext) {
        this.identityClass = identityClass;
        this.sessionContext = sessionContext;
    }

    @Override
    public boolean existSession() {
        return sessionContext.getByType(identityClass).isPresent();
    }

    @Override
    public final SignedTranslatable<?> translateExpire() {
        sessionContext.removeObject(identityClass);
        return doOnExpire();
    }

    @Override
    public final void onAuth(AuthIdentity identity) {
        sessionContext.setObject(identity);
        doOnAuth();
    }

    protected abstract SignedTranslatable<?> doOnExpire();

    protected abstract void doOnAuth();
}
