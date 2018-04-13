package org.carbon.sample.v2.app.oauth.client;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.v2.exception.DuplicateEntityException;
import org.carbon.sample.v2.app.oauth.AuthClientDto;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Validate;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Redirect;
import org.carbon.web.translate.dto.Transfer;

/**
 * @author Shota Oda 2017/07/29.
 */
@Controller("/oauth/client")
public class OAuth2ClientController {
    @Assemble
    private OAuth2ClientService service;

    @Action(path = "/console", method = HttpMethod.GET)
    public Html get() {

        List<AuthClientDto> clients = service.fetchAllClient();
        Html html = new Html("/oauth/client/console");
        html.putData("clients", clients);
        return html;
    }

    @Action(path = "/register", method = HttpMethod.GET)
    public Transfer getClientRegister() {
        return new Redirect("/oauth/client/console");
    }

    @Action(path = "/register", method = HttpMethod.POST)
    public Html postClientId(
            @Validate @RequestBody OAuth2ClientRegisterForm form,
            HandyValidationResult vr
    ) {
        // validation
        Html html;
        if (vr.existError()) {
            html = this.get();
            html.putData("errors", vr.getViolationResults());
            return html;
        }

        // check database
        try {
            String clientId = service.registerClientHost(form.getClientHost());
            html = this.get();
            html.putData("clientId", clientId);
        } catch (DuplicateEntityException e) {
            html = this.get();
            html.putData("form", form);
            Map<String, String> errors = new HashMap<>();
            errors.put("clientHost", "this host is already registered");
            html.putData("errors", errors);
        }
        return html;
    }

}
