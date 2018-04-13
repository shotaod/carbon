package org.carbon.sample.auth.basic.identity;

import org.carbon.authentication.AuthIdentity;
import org.carbon.sample.ext.jooq.tables.pojos.User;

/**
 * @author Shota Oda 2016/11/03.
 */
public class SampleBasicAuthIdentity implements AuthIdentity {

    private User user;

    public SampleBasicAuthIdentity(User user) {
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
