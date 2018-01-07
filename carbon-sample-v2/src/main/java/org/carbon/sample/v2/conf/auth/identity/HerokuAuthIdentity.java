package org.carbon.sample.v2.conf.auth.identity;

import org.carbon.authentication.AuthIdentity;
import org.carbon.sample.v2.ext.jooq.tables.pojos.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota Oda 2016/11/03.
 */
public class HerokuAuthIdentity implements AuthIdentity {

    private User user;

    public HerokuAuthIdentity(User user) {
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public String username() {
        return user.getUserName();
    }

    @Override
    public String cryptPassword() {
        return user.getPassword();
    }

    @Override
    public boolean confirm(String plainPassword) {
        return BCrypt.checkpw(plainPassword, cryptPassword());
    }
}
