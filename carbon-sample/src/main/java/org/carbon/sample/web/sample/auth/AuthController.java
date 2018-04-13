package org.carbon.sample.web.sample.auth;

import org.carbon.sample.auth.basic.identity.SampleBasicAuthIdentity;
import org.carbon.sample.auth.form.identity.FormAuthIdentity;
import org.carbon.sample.web.sample.index.UserInfoModel;
import org.carbon.sample.web.session.SessionInfo;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.scope.SessionScope;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2016/11/05.
 */
@Controller
public class AuthController {
    // -----------------------------------------------------
    //                                               Basic Auth
    //                                               -------
    @Action(path = "/basic", method = HttpMethod.GET)
    public Html getBasicIndex() {
        return new Html("/basic/index");
    }

    @Action(path = "/basic/secret/{number}", method = HttpMethod.GET)
    public Html requestBasicSecret(@PathVariable("number") String number, @SessionScope SampleBasicAuthIdentity userSession) {
        Html response = new Html("basic/secret");

        UserInfoModel model = new UserInfoModel();
        model.setUsername(userSession.identity());
        model.setPassword(userSession.getPassword());

        response.putData("model", model);
        response.putData("secretNumber", number);
        return response;
    }

    @Action(path = "/form", method = HttpMethod.GET)
    public Html getLogin() {
        return new Html("form/login");
    }

    @Action(path = "/form/auth", method = HttpMethod.POST)
    public Html postFormAuth() {
        return new Html("/form/index");
    }

    @Action(path = "/form/secret", method = HttpMethod.GET)
    public Html getFormSecret(
            @SessionScope FormAuthIdentity userSession,
            @SessionScope SessionInfo sessionInfo) {
        Html response = new Html("/form/secret");
        UserInfoModel model = new UserInfoModel();
        model.setUsername(userSession.identity());
        model.setPassword(userSession.getPassword());
        response.putData("model", model);
        response.putData("loginInfo", sessionInfo);
        return response;
    }
}
