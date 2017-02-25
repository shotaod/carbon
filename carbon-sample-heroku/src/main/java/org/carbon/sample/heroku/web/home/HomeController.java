package org.carbon.sample.heroku.web.home;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.def.HttpMethod;

/**
 * @author ubuntu 2017/02/23.
 */
@Controller
public class HomeController {
    @Action(url = "/home", method = HttpMethod.GET)
    public HtmlResponse getHome() {
        return new HtmlResponse("/home/index");
    }
}
