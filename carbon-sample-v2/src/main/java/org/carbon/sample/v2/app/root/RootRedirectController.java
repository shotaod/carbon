package org.carbon.sample.v2.app.root;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Redirect;
import org.carbon.web.translate.dto.Transfer;

/**
 * @author Shota Oda 2017/02/12.
 */
@Controller
public class RootRedirectController {

    /**
     * GET end point at '/'(root)
     * @return redirect to home
     */
    @Action(path = "/", method = HttpMethod.GET)
    public Transfer redirectToMessage() {
        return new Redirect("/home");
    }

    /**
     * Test end point for cause internal server error
     */
    @Action(path = "/cause/error", method = HttpMethod.GET)
    public void throwException() {
        throw new RuntimeException("Exception for purpose of 500 page test");
    }
}
