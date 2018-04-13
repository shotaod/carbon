package org.carbon.sample.v2.app.oauth.repository;

import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.carbon.sample.v2.app.oauth.def.AuthScope;

/**
 * @author Shota Oda 2017/08/11.
 */
public class AccessCode extends Expiration {
    private String host;
    private String code;
    private Set<AuthScope> authScopes;

    public AccessCode(String host, String code, Set<AuthScope> authScopes) {
        super(5, ChronoUnit.MINUTES);
        this.host = host;
        this.code = code;
        this.authScopes = authScopes;
    }

    public String getHost() {
        return host;
    }

    public String getCode() {
        return code;
    }

    public Set<AuthScope> getAuthScopes() {
        return authScopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccessCode)) return false;

        AccessCode that = (AccessCode) o;

        //noinspection SimplifiableIfStatement
        if (!host.equals(that.host)) return false;
        return code.equals(that.code);

    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
