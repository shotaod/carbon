package org.carbon.sample.v2.web.oauth.repository;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Component;
import org.carbon.sample.v2.web.oauth.def.AuthScope;

/**
 * @author Shota Oda 2017/08/10.
 */
@Component
public class AccessTokenRepository {
    private class AccessTokenKey extends Expiration {
        private String host;
        private String token;

        public AccessTokenKey(String host, String token) {
            super(60, ChronoUnit.MINUTES);
            this.host = host;
            this.token = token;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AccessTokenKey)) return false;

            AccessTokenKey that = (AccessTokenKey) o;

            if (!host.equals(that.host)) return false;
            return token.equals(that.token);

        }

        @Override
        public int hashCode() {
            int result = host.hashCode();
            result = 31 * result + token.hashCode();
            return result;
        }
    }
    private ConcurrentHashMap<AccessTokenKey, Set<AuthScope>> accessTokens;

    public void save(AccessToken token) {
        save(token.getHost(), token.getToken(), token.getAuthScopes());
    }

    public void save(String host, String accessToken, AuthScope... grantTypes) {
        Set<AuthScope> set = Stream.of(grantTypes).collect(Collectors.toSet());
        save(host, accessToken, set);
    }

    public void save(String host, String accessToken, Set<AuthScope> grantTypes) {
        if (accessTokens == null) {
            accessTokens = new ConcurrentHashMap<>();
        }
        accessTokens.put(new AccessTokenKey(host, accessToken), grantTypes);
    }

    public Set<AuthScope> getAuthScope(String host, String accessToken) {
        if (accessTokens == null) {
            return Collections.emptySet();
        }

        AccessTokenKey target = new AccessTokenKey(host, accessToken);
        return accessTokens.search(1, (key, scopes) -> key.equals(target) ? scopes : Collections.emptySet());
    }
}
