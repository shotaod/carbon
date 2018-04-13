package org.carbon.sample.web.message;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/01/08.
 */
@Controller
public class MessageController {

    @Action(path = "/message", method = HttpMethod.GET)
    public Html messageGet() {
        return new Html("/message/room");
    }
}
