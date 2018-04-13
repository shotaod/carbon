package org.carbon.sample.v2.app.user;

import java.util.HashMap;
import java.util.Map;

import org.carbon.component.annotation.Assemble;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.annotation.Validate;
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.translate.dto.Redirect;
import org.carbon.web.translate.dto.Transfer;

/**
 * @author Shota Oda 2017/02/13.
 */
@Controller("/user")
public class UserController {
    @Assemble
    private UserService service;

    @Action(path = "/login", method = HttpMethod.GET)
    public Html loginGet() {
        return new Html("/user/login");
    }

    @Action(path = "/login", method = HttpMethod.POST)
    public Transfer loginSuccess() {
        return new Redirect("/user/secret");
    }

    @Action(path = "/signup", method = HttpMethod.GET)
    public Html signupGet() {
        return new Html("/user/signup");
    }

    @Action(path = "/signup", method = HttpMethod.POST)
    public Html signupPost(@Validate @RequestBody UserSignUpForm form, HandyValidationResult vr) {
        Html retry = new Html("/user/signup");
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

        Html success = new Html("/user/signup_success");
        success.putData("data", form);
        return success;
    }

    @Action(path = "/secret", method = HttpMethod.GET)
    public Html secretGet() {
        return new Html("/user/secret");
    }
}
