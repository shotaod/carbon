package org.carbon.sample.v2.app.sample.websocket;

import org.carbon.web.annotation.ConfigureChannel;
import org.carbon.web.annotation.OnClose;
import org.carbon.web.annotation.OnOpen;
import org.carbon.web.annotation.OnReceive;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.Socket;
import org.carbon.web.ws.ChannelConfiguration;
import org.carbon.web.ws.Message;

/**
 * @author Shota Oda 2017/02/12.
 */
@Socket(path = "/message/socket/{userName}/{roomId}", protocols = "soap")
public class ChatSocket {

    @OnOpen
    public void onConnect() {
        // do something ...
    }

    @OnClose
    public void onClose() {
        // do something ...
    }

    @OnReceive
    public MessageDto onReceive(@PathVariable("roomId") String roomId, Message message) {
        return new MessageDto(message.getFrom().getKey(), message.getContent());
    }

    @ConfigureChannel
    public void config(@PathVariable("userName") String userName,
                       @PathVariable("roomId") String roomId,
                       ChannelConfiguration channelConfiguration
    ) {

    }
}
