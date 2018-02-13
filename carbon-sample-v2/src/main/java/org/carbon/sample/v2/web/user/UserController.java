package org.carbon.sample.v2.web.user;

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
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/13.
 */
@Controller("/user")
public class UserController {
    @Inject
    private UserService service;

    @Action(url = "/login", method = HttpMethod.GET)
    public HtmlResponse loginGet() {
        return new HtmlResponse("/user/login");
    }

    @Action(url = "/login", method = HttpMethod.POST)
    public HttpOperation loginSuccess() {
        return RedirectOperation.to("/user/secret");
    }

    @Action(url = "/signup", method = HttpMethod.GET)
    public HtmlResponse signupGet() {
        return new HtmlResponse("/user/signup");
    }

    @Action(url = "/signup", method = HttpMethod.POST)
    public HtmlResponse signupPost(@Validate @RequestBody UserSignUpForm form, HandyValidationResult vr) {
        HtmlResponse retry = new HtmlResponse("/user/signup");
        if (vr.existError()) {
            retry.putData("error", vr.getViolationResults());
            return retry;
        } else if (!form.isIllegalPassword()) {
            Map<String, String> error = new HashMap<>();
            error.put("password", "Password is not match.");
            retry.putData("error", error);
            return retry;
        }

        service.registerUser(form);

        HtmlResponse success = new HtmlResponse("/user/signup_success");
        success.putData("data", form);
        return success;
    }

    @Action(url = "/secret", method = HttpMethod.GET)
    public HtmlResponse secretGet() {
        return new HtmlResponse("/user/secret");
    }
}
