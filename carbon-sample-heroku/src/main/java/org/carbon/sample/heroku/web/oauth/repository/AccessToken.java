package org.carbon.sample.heroku.web.oauth.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.carbon.sample.heroku.web.oauth.def.AuthScope;

/**
 * @author Shota Oda 2017/08/11.
 */
public class AccessToken extends RefreshableExpiration<String> {
    private static final int EXPIRE_MIN = 60;

    private String token;
    private String host;
    private Set<AuthScope> authScopes;

    public AccessToken(String token, String refreshToken, String host, Set<AuthScope> authScopes) {
        super(EXPIRE_MIN, ChronoUnit.MINUTES, refreshToken);
        this.token = token;
        this.host = host;
        this.authScopes = authScopes;
    }

    public static int getExpireMin() {
        return EXPIRE_MIN;
    }

    public String getToken() {
        return token;
    }

    public String getHost() {
        return host;
    }

    public Set<AuthScope> getAuthScopes() {
        return authScopes;
    }

    @Override
    public boolean reValidate(String refreshToken) {
        if (refresh_token.equals(refreshToken)) {
            this.expire = LocalDateTime.now().plusMinutes(EXPIRE_MIN);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessToken)) return false;

        AccessToken that = (AccessToken) o;

        if (!token.equals(that.token)) return false;
        return host.equals(that.host);
    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + host.hashCode();
        return result;
    }
}
