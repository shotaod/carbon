package org.carbon.authentication.strategy;

import org.carbon.authentication.translator.SignedTranslatable;

/**
 * @author Shota.Oda 2018/02/25.
 */
public interface AuthFailureTranslator {
    SignedTranslatable<?> translateExpire();

    SignedTranslatable<?> translateProhibitNoAuth();

    SignedTranslatable<?> translateIllegalAuthRequest();

    SignedTranslatable<?> translateNoFoundIdentity();

    SignedTranslatable<?> translateNoMatchSecret();
}
