package org.carbon.sample.heroku.web.oauth;

import org.carbon.component.annotation.Inject;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestHeader;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.def.HttpMethod;

/**
 * @author garden 2017/07/16.
 */
@Controller("/oauth")
public class OAuth2Controller {

    @Inject
    private OAuth2Service service;

    @Action(url = "authorize", method = HttpMethod.GET)
    public HtmlResponse getAuthorize(
            @RequestHeader("host") String host,
            @Validate @RequestParam OAuth2Param param,
            SimpleValidationResult vr
    ) {
        if (vr.existError() || !service.validateClient(host, param.getClient_id())) {
            return new HtmlResponse("error/403");
        }

        HtmlResponse htmlResponse = new HtmlResponse("/oauth/authorize");
        htmlResponse.putData("host", host);
        htmlResponse.putData("scopes", param.getScopes());
        return htmlResponse;
    }
}
