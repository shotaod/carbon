package org.carbon.sample.auth.form.identity;

import org.carbon.sample.ext.jooq.tables.pojos.User;
import org.carbon.web.auth.AuthIdentity;

/**
 * @author Shota Oda 2016/11/03.
 */
public class FormAuthIdentity implements AuthIdentity {

    private User user;

    public FormAuthIdentity(User user) {
        this.user = user;
    }

    @Override
    public String username() {
        return user.getUsername();
    }

    @Override
    public String cryptPassword() {
        return user.getPassword();
    }

    @Override
    public boolean confirm(String plainPassword) {
        return cryptPassword().equals(plainPassword);
    }
}
