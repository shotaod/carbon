package org.carbon.sample.v2.app.api.rocketty.auth;

import org.carbon.component.annotation.Assemble;
import org.carbon.sample.v2.app.api.rocketty.SuccessMessageDTO;
import org.carbon.sample.v2.app.api.rocketty.auth.exception.IllegalClientAuthException;
import org.carbon.sample.v2.app.api.rocketty.auth.req.AuthConfirmDTO;
import org.carbon.sample.v2.app.api.rocketty.auth.req.AuthRegisterDTO;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Validate;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Json;

/**
 * @author Shota.Oda 2018/02/15.
 */
@Controller("/api/v1/rocketty/auth")
public class RockettyAuthController {

    @Assemble
    private RockettyAuthAppService rockettyAuthAppService;

    @Action(method = HttpMethod.POST, path = "")
    public Json postAuth() {
        return new SuccessMessageDTO("successfully authorized");
    }

    @Action(method = HttpMethod.POST, path = "/register")
    public Json postAuthRegister(
            @Validate @RequestBody AuthRegisterDTO authRegisterBody
    ) throws IllegalClientAuthException {
        return rockettyAuthAppService.registerClient(authRegisterBody);
    }

    @Action(method = HttpMethod.POST, path = "/confirm")
    public Json postAuthConfirm(
            @Validate @RequestBody AuthConfirmDTO authConfirmDTO
    ) throws IllegalClientAuthException {
        return rockettyAuthAppService.confirmClient(authConfirmDTO);
    }
}
