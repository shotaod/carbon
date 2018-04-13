package org.carbon.sample.v2.app.oauth;

import java.util.HashMap;
import java.util.Map;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.v2.util.URLUtil;
import org.carbon.sample.v2.app.oauth.client.AccessTokenDto;
import org.carbon.sample.v2.app.oauth.def.GrantType;
import org.carbon.sample.v2.app.oauth.def.ResponseType;
import org.carbon.sample.v2.app.oauth.repository.AccessToken;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestHeader;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.annotation.Validate;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Redirect;

/**
 * @author Shota Oda 2017/07/16.
 */
@Controller("/oauth")
public class OAuth2Controller {

    @Assemble
    private OAuth2Service service;

    @Action(path = "/authorize", method = HttpMethod.GET)
    public Object getAuthorize(
            @RequestHeader("host") String host,
            @Validate @RequestParam OAuth2Param param,
            HandyValidationResult vr
    ) {
        if (vr.existErrorFor("redirect_uri")) {
            return new Html("error/403");
        }
        if (vr.existError() || !service.validateClient(host, param.getClient_id())) {
            String redirectUrlWithErrorCode = URLUtil.appendParameter(
                    param.getRedirect_uri(),
                    "error",
                    OAuth2Exception.invalidRequest().getErrorCode());
            return new Redirect(redirectUrlWithErrorCode);
        }

        Html html = new Html("/oauth/authorize");
        html.putData("data", new OAuth2AuthorizeResourceOwnerDto(host, param));
        return html;
    }

    @Action(path = "/authorize", method = HttpMethod.POST)
    public Object postAuthorize(
            @Validate @RequestBody OAuth2PermitForm permitForm,
            HandyValidationResult vr
    ) {
        Html errorResponse = new Html("/oauth/authorize");
        errorResponse.putData("data", new OAuth2AuthorizeResourceOwnerDto(permitForm));
        if (vr.existError()) {
            errorResponse.putData("errors", vr.getViolationResults());
            return errorResponse;
        }
        if (!service.checkUser(permitForm)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("auth", "Email or password is invalid. Please try again.");
            errorResponse.putData("errors", errors);
            return errorResponse;
        }

        if (permitForm.getResponse_type().equals(ResponseType.code.getValue())) {
            String code = service.publishAuthorizationCode(permitForm);

            String redirectTo = URLUtil.appendParameter(permitForm.getRedirect_uri(), "code", code);
            return new Redirect(redirectTo);
        }

        return new Html("errors/403");
    }

    @Action(path = "/token", method = HttpMethod.POST)
    public Object getToken(
            @Validate @RequestBody OAuth2TokenForm form,
            HandyValidationResult vr
    ) {
        try {
            if (vr.existError()) {
                throw OAuth2Exception.invalidRequest();
            }

            GrantType grantType = GrantType.valueOf(form.getGrant_type());
            AccessToken accessToken = null;
            if (grantType == GrantType.authorization_code) {
                accessToken = service.publishAccessToken(form);
            } else if (grantType == GrantType.refresh_token) {
                accessToken = service.refreshAccessToken(form);
            }

            if (accessToken != null) {
                return new AccessTokenDto(accessToken);
            }

            throw OAuth2Exception.unsupportedGrantType();
        } catch (OAuth2Exception e) {
            return new OAuth2AccessTokenErrorResponse(e);
        }
    }
}
