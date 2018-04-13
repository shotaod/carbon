package org.carbon.authentication.strategy;

import org.carbon.authentication.AuthIdentity;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthEventListener {
    void onPermitNoAuth();

    /**
     * event handler for on existSession
     */
    void onExistSession();

    /**
     * event handler for on authenticated
     */
    void onAuth(AuthIdentity identity);
}
