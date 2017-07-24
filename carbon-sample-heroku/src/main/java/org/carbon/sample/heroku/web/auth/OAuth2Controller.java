package org.carbon.sample.heroku.web.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.exception.DuplicateEntityException;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.RequestHeader;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.util.ResponseUtil;

/**
 * @author garden 2017/07/16.
 */
@Controller("/auth")
public class OAuth2Controller {

    @Inject
    private OAuth2Service service;

    @Action(url = "/client/register", method = HttpMethod.GET)
    public HttpOperation getClientRegister() {
        return RedirectOperation.to("/auth/client/console");
    }

    @Action(url = "/client/console", method = HttpMethod.GET)
    public HtmlResponse get() {

        List<AuthClientDto> clients = service.fetchAllClient();
        HtmlResponse htmlResponse = new HtmlResponse("/auth/client/console");
        htmlResponse.putData("clients", clients);
        return htmlResponse;
    }

    @Action(url = "/client/register", method = HttpMethod.POST)
    public HtmlResponse postClientId(
            @Validate @RequestBody OAuth2ClientRegisterForm form,
            SimpleValidationResult vr
    ) {
        // validation
        HtmlResponse htmlResponse;
        if (vr.existError()) {
            htmlResponse = this.get();
            htmlResponse.putAll(vr.getViolationResults());
            return htmlResponse;
        }

        // check database
        try {
            String clientId = service.registerClientHost(form.getClientHost());
            htmlResponse = this.get();
            htmlResponse.putData("clientId", clientId);
        } catch (DuplicateEntityException e) {
            htmlResponse = this.get();
            htmlResponse.putData("form", form);
            Map<String, String> errors = new HashMap<>();
            errors.put("clientHost", "this host is already registered");
            htmlResponse.putData("errors", errors);
        }
        return htmlResponse;
    }

    @Action(url = "authorize", method = HttpMethod.GET)
    public HtmlResponse getAuthorize(
            @RequestHeader("host") String host,
            @Validate @RequestParam OAuth2Param param,
            SimpleValidationResult vr
    ) {
        if (vr.existError() || !service.validateClient(host, param.getClient_id())) {
            return new HtmlResponse("error/403");
        }

        HtmlResponse htmlResponse = new HtmlResponse("/auth/authorize");
        htmlResponse.putData("host", host);
        htmlResponse.putData("scopes", param.getScopes());
        return htmlResponse;
    }
}
