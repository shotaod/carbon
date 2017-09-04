package org.carbon.sample.heroku.web.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Shota Oda 2017/07/30.
 */
public class OAuth2Exception extends Exception {
    @AllArgsConstructor
    private enum ErrorCode {
        invalid_request("invalid_request"),
        invalid_client("invalid_client"),
        unauthorized_client("unauthorized_client"),
        redirect_uri_mismatch("redirect_uri_mismatch"),
        access_denied("access_denied"),
        unsupported_response_type("unsupported_response_type"),
        unsupported_grant_type("unsupported_grant_type"),
        invalid_scope("invalid_scope"),
        ;
        private String value;
    }

    @Getter
    private String errorCode;

    private OAuth2Exception(ErrorCode errorCode) {
        super("OAuth2 Exception reason: " + errorCode);
        this.errorCode = errorCode.value;
    }

    public static OAuth2Exception invalidRequest() {
        return new OAuth2Exception(ErrorCode.invalid_request);
    }
    public static OAuth2Exception invalidClient() {
        return new OAuth2Exception(ErrorCode.invalid_client);
    }
    public static OAuth2Exception unauthorizedClient() {
        return new OAuth2Exception(ErrorCode.unauthorized_client);
    }
    public static OAuth2Exception redirectUriMismatch() {
        return new OAuth2Exception(ErrorCode.redirect_uri_mismatch);
    }
    public static OAuth2Exception accessDenied() {
        return new OAuth2Exception(ErrorCode.access_denied);
    }
    public static OAuth2Exception unsupportedResponseType() {
        return new OAuth2Exception(ErrorCode.unsupported_response_type);
    }
    public static OAuth2Exception unsupportedGrantType() {
        return new OAuth2Exception(ErrorCode.unsupported_grant_type);
    }

    public static OAuth2Exception invalidScope() {
        return new OAuth2Exception(ErrorCode.invalid_scope);
    }
}
