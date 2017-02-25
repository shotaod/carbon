package org.carbon.sample.heroku.web.sample.security;

import java.util.HashMap;
import java.util.Map;

import org.carbon.component.annotation.Inject;
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
 * @author Shota Oda 2017/02/13.
 */
@Controller("/sample/security")
public class SecurityController {
    @Inject
    private UserService service;

    @Action(url = "/login", method = HttpMethod.GET)
    public HtmlResponse loginGet() {
        return new HtmlResponse("/sample/security/login");
    }

    @Action(url = "/login", method = HttpMethod.POST)
    public HttpOperation loginSuccess() {
        return RedirectOperation.to("/sample/security/secret");
    }

    @Action(url = "/signup", method = HttpMethod.GET)
    public HtmlResponse signupGet() {
        return new HtmlResponse("/sample/security/signup");
    }

    @Action(url = "/signup", method = HttpMethod.POST)
    public HtmlResponse signupPost(@Validate @RequestBody UserSignUpForm form, SimpleValidationResult vr) {
        HtmlResponse retry = new HtmlResponse("/sample/security/signup");
        if (vr.existError()) {
            retry.putData("error", vr.getViolationResults());
            return retry;
        } else if (!form.isIllegalPassword()){
            Map<String, String> error = new HashMap<>();
            error.put("password", "Password is not match.");
            retry.putData("error", error);
            return retry;
        }

        service.registerUser(form);

        HtmlResponse success = new HtmlResponse("/sample/security/signup_success");
        success.putData("data", form);
        return success;
    }

    @Action(url = "/secret", method = HttpMethod.GET)
    public HtmlResponse secretGet() {
        return new HtmlResponse("/sample/security/secret");
    }
}
