package org.carbon.authentication.strategy;

import org.carbon.authentication.AuthIdentity;

/**
 * @author Shota Oda 2016/11/03.
 */
public interface AuthEventListener {
    void onPermitNoAuth();

    void onExpire();

    void onExistSession();

    void onProhibitNoAuth();

    void onIllegalAuthRequest();

    void onNoFoundIdentity();

    void onAuth(AuthIdentity identity);

    void onNoMatchSecret();
}
