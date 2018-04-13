package org.carbon.sample.v2.app.sample.websocket;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.translate.dto.Html;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/12.
 */
@Controller("/sample/websocket")
public class ChatController {

    @Action(path = "", method = HttpMethod.GET)
    public Html messageGet() {
        return new Html("/sample/websocket/room");
    }
}
