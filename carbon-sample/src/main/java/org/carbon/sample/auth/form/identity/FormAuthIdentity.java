package org.carbon.sample.auth.form.identity;

import org.carbon.authentication.AuthIdentity;
import org.carbon.sample.ext.jooq.tables.pojos.User;

/**
 * @author Shota Oda 2016/11/03.
 */
public class FormAuthIdentity implements AuthIdentity {

    private User user;

    public FormAuthIdentity(User user) {
        this.user = user;
    }

    @Override
    public String identity() {
        return user.getUsername();
    }

    @Override
    public boolean confirm(String plainSecret) {
        return user.getPassword().equals(plainSecret);
    }

    public String getPassword() {
        return user.getPassword();
    }
}
