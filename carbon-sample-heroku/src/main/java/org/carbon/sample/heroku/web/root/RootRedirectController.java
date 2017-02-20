package org.carbon.sample.heroku.web.root;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/12.
 */
@Controller
public class RootRedirectController {
    @Action(url = "/", method = HttpMethod.GET)
    public HttpOperation redirectToMessage() {
        return RedirectOperation.to("/login");
    }

    @Action(url = "/cause/error", method = HttpMethod.GET)
    public void throwException() {
        throw new RuntimeException("Exception for purpose of 500 page test");
    }
}
