package org.carbon.sample.v2.infra.secured;

import org.carbon.component.annotation.Component;
import org.carbon.sample.v2.conf.auth.SecureStrings;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota.Oda 2018/03/01.
 */
@Component
public class BCryptSecureStrings implements SecureStrings {

    @Override
    public String hash(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt());
    }

    @Override
    public boolean check(String raw, String secured) {
        return BCrypt.checkpw(raw, secured);
    }
}
