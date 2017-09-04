package org.carbon.sample.heroku.web.oauth.client;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carbon.component.annotation.Inject;
import org.carbon.sample.heroku.exception.DuplicateEntityException;
import org.carbon.sample.heroku.web.oauth.AuthClientDto;
import org.carbon.sample.heroku.web.oauth.OAuth2Service;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.core.validation.SimpleValidationResult;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/07/29.
 */
@Controller("/oauth/client")
public class OAuth2ClientController {
    @Inject
    private OAuth2ClientService service;

    @Action(url = "/console", method = HttpMethod.GET)
    public HtmlResponse get() {

        List<AuthClientDto> clients = service.fetchAllClient();
        HtmlResponse htmlResponse = new HtmlResponse("/oauth/client/console");
        htmlResponse.putData("clients", clients);
        return htmlResponse;
    }

    @Action(url = "/register", method = HttpMethod.GET)
    public HttpOperation getClientRegister() {
        return RedirectOperation.to("/oauth/client/console");
    }

    @Action(url = "/register", method = HttpMethod.POST)
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

}
