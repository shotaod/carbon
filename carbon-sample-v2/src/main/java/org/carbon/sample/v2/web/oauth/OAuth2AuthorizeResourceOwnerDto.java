package org.carbon.sample.v2.web.oauth;

import java.util.Set;

import lombok.Getter;
import org.carbon.sample.v2.web.oauth.def.AuthScope;

/**
 * @author Shota Oda 2017/07/30.
 */
@Getter
public class OAuth2AuthorizeResourceOwnerDto {
    private String email;
    private String response_type;
    private String host;
    private String client_id;
    private Set<AuthScope> scopes;
    private String redirect_uri;

    public OAuth2AuthorizeResourceOwnerDto(String host, OAuth2Param param) {
        this.response_type = param.getResponse_type();
        this.host = host;
        this.client_id = param.getClient_id();
        this.redirect_uri = param.getRedirect_uri();
        this.scopes = param.getScopes();
    }

    public OAuth2AuthorizeResourceOwnerDto(OAuth2PermitForm form) {
        this.email = form.getEmail();
        this.response_type = form.getResponse_type();
        this.host = form.getHost();
        this.client_id = form.getClient_id();
        this.scopes = form.getScopes();
        this.redirect_uri = form.getRedirect_uri();
    }
}
