package org.carbon.sample.v2.conf.auth.api;

import org.carbon.authentication.AuthIdentity;
import org.carbon.sample.v2.ext.jooq.tables.pojos.RockettyAuthClient;

/**
 * @author Shota.Oda 2018/02/15.
 */
public class RockettyClientIdentity implements AuthIdentity {

    private RockettyAuthClient authClient;

    public RockettyClientIdentity(RockettyAuthClient authClient) {
        this.authClient = authClient;
    }

    public Long getId() {
        return authClient.getId();
    }

    @Override
    public String identity() {
        return authClient.getClientId();
    }

    @Override
    public boolean confirm(String plainSecret) {
        return plainSecret.equals(authClient.getClientSecret());
    }
}
