package org.carbon.authentication.strategy.request;

/**
 * @author garden 2018/02/12.
 */
public class AuthRequest {
    private String identity;
    private String secret;

    public AuthRequest(String identity, String secret) {
        this.identity = identity;
        this.secret = secret;
    }

    public String getIdentity() {
        return identity;
    }

    public String getSecret() {
        return secret;
    }
}
