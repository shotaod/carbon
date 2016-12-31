package org.carbon.sample.web.root;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.core.response.RedirectOperation;
import org.carbon.web.def.HttpMethod;

/**
 * @author ubuntu 2016/12/18.
 */
@Controller
public class RootController {
    @Action(url = "/", method = HttpMethod.GET)
    public HttpOperation redirectConsumer() {
        return RedirectOperation.to("/consumer");
    }
}
