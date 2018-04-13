package org.carbon.sample.v2.app.home;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/23.
 */
@Controller
public class HomeController {
    @Action(path = "/home", method = HttpMethod.GET)
    public Html getHome() {
        return new Html("/home/index");
    }
}
