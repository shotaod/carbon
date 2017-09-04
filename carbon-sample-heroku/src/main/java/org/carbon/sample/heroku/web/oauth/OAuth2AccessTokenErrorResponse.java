package org.carbon.sample.heroku.web.oauth;

import lombok.Getter;

/**
 * @author Shota Oda 2017/08/11.
 */
@Getter
public class OAuth2AccessTokenErrorResponse {
    private String error;

    public OAuth2AccessTokenErrorResponse(OAuth2Exception ex) {
        this.error = ex.getErrorCode();
    }
}
