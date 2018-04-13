package org.carbon.sample.web.root;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.translate.dto.Redirect;
import org.carbon.web.translate.dto.Transfer;

/**
 * @author Shota Oda 2016/12/18.
 */
@Controller
public class RootController {
    @Action(path = "/", method = HttpMethod.GET)
    public Transfer redirectConsumer() {
        return new Redirect("/consumer");
    }
}
