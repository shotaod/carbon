package org.carbon.sample.web.message;

import org.carbon.web.annotation.ConfigureChannel;
import org.carbon.web.annotation.OnClose;
import org.carbon.web.annotation.OnOpen;
import org.carbon.web.annotation.OnReceive;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.annotation.Socket;
import org.carbon.web.ws.ChannelConfiguration;
import org.carbon.web.ws.Message;

/**
 * @author Shota Oda 2017/01/01.
 */
@Socket(path = "/message/socket/{userName}/{roomId}", protocols = "soap")
public class MessageSocket {

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
                       @PathVariable("roomId") String roomId) {
        ChannelConfiguration.simple(roomId, userName);
    }
}
