package org.carbon.sample.heroku.web.oauth;

import java.util.HashMap;
import java.util.Map;

import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.util.URLUtil;
import org.carbon.sample.heroku.web.oauth.client.AccessTokenDto;
import org.carbon.sample.heroku.web.oauth.def.GrantType;
import org.carbon.sample.heroku.web.oauth.def.ResponseType;
import org.carbon.sample.heroku.web.oauth.repository.AccessToken;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestHeader;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/07/16.
 */
@Controller("/oauth")
public class OAuth2Controller {

    @Inject
    private OAuth2Service service;

    @Action(url = "/authorize", method = HttpMethod.GET)
    public Object getAuthorize(
            @RequestHeader("host") String host,
            @Validate @RequestParam OAuth2Param param,
            SimpleValidationResult vr
    ) {
        if (vr.existErrorFor("redirect_uri")) {
            return new HtmlResponse("error/403");
        }
        if (vr.existError() || !service.validateClient(host, param.getClient_id())) {
            String redirectUrlWithErrorCode = URLUtil.appendParameter(
                    param.getRedirect_uri(),
                    "error",
                    OAuth2Exception.invalidRequest().getErrorCode());
            return RedirectOperation.to(redirectUrlWithErrorCode);
        }

        HtmlResponse htmlResponse = new HtmlResponse("/oauth/authorize");
        htmlResponse.putData("data", new OAuth2AuthorizeResourceOwnerDto(host, param));
        return htmlResponse;
    }

    @Action(url = "/authorize", method = HttpMethod.POST)
    public Object postAuthorize(
            @Validate @RequestBody OAuth2PermitForm permitForm,
            SimpleValidationResult vr
    ) {
        HtmlResponse errorResponse = new HtmlResponse("/oauth/authorize");
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
            return RedirectOperation.to(redirectTo);
        }

        return new HtmlResponse("errors/403");
    }

    @Action(url = "/token", method = HttpMethod.POST)
    public Object getToken(
            @Validate @RequestBody OAuth2TokenForm form,
            SimpleValidationResult vr
    ) {
        try {
            if (vr.existError()) {
                throw OAuth2Exception.invalidRequest();
            }

            GrantType grantType = GrantType.valueOf(form.getGrant_type());
            AccessToken accessToken = null;
            if (grantType == GrantType.authorization_code) {
                accessToken = service.publishAccessToken(form);
            } else if (grantType == GrantType.refresh_token){
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
