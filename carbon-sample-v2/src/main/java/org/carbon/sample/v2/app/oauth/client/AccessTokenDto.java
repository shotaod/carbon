package org.carbon.sample.v2.app.oauth.client;

import java.util.stream.Collectors;

import org.carbon.sample.v2.app.oauth.def.AuthScope;
import org.carbon.sample.v2.app.oauth.repository.AccessToken;

/**
 * @author Shota Oda 2017/08/11.
 */
public class AccessTokenDto {
    private String access_token;
    private String refresh_token;
    private int expires_in;
    private String scope;

    public AccessTokenDto(AccessToken accessToken) {
        this.access_token = accessToken.getToken();
        this.refresh_token = accessToken.getRefreshToken();
        this.expires_in = AccessToken.getExpireMin() * 60;
        this.scope = accessToken.getAuthScopes().stream()
                .map(AuthScope::getCode)
                .collect(Collectors.joining(" "));
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }
}
