package org.carbon.sample.heroku.conf.auth.identity;

import org.carbon.sample.heroku.ext.jooq.tables.pojos.User;
import org.carbon.web.auth.AuthIdentity;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Shota Oda 2016/11/03.
 */
public class HerokuAuthIdentity implements AuthIdentity {

    private User user;

    public HerokuAuthIdentity(User user) {
        this.user = user;
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
