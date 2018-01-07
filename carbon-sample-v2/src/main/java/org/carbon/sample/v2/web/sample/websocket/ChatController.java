package org.carbon.sample.v2.web.sample.websocket;

import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/02/12.
 */
@Controller("/sample/websocket")
public class ChatController {

    @Action(url = "", method = HttpMethod.GET)
    public HtmlResponse messageGet() {
        return new HtmlResponse("/sample/websocket/room");
    }
}
