package org.carbon.web.ws;

import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.container.ExecutableAction;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/01/07.
 */
public class ChannelWebSocketAction extends AbstractReceiver implements WebSocketListener, Receiver {
    private Logger logger = LoggerFactory.getLogger(ChannelWebSocketAction.class);
    private static final ChannelStation station = ChannelStation.instance;
    private ObjectMapper objectMapper;
    private ExecutableAction<Void> onOpen;
    private ExecutableAction<Void> onClose;
    private ExecutableAction<Object> onReceive;
    private ExecutableAction<ChannelConfiguration> configurer;

    public ChannelWebSocketAction(
            ObjectMapper objectMapper,
            ExecutableAction<Object> onReceive,
            ExecutableAction<Void> onOpen,
            ExecutableAction<Void> onClose,
            ExecutableAction<ChannelConfiguration> configurer
    ) {
        this.objectMapper = objectMapper;
        this.onOpen = onOpen;
        this.onClose = onClose;
        this.onReceive = onReceive;
        this.configurer = configurer;
    }

    private <T> T call(ExecutableAction<T> action) {
        if (action != null)
            return action.execute();
        return null;
    }

    // ===================================================================================
    //                                                                          WebSocketListener
    //                                                                          ==========
    private Session session;

    @Override
    public void onWebSocketConnect(Session session) {
        logger.debug("[WebSocket] Connection Open -> \nsession:\n{}", session);
        this.session = session;
        station.register(this);
        call(onOpen);
    }

    @Override
    public void onWebSocketClose(int closeCode, String message) {
        logger.debug("[WebSocket] Connection Close -> \ncode: {}\nmessage: {}", closeCode, message);
        station.unregister(this);
        call(onClose);
    }

    @Override
    public void onWebSocketError(Throwable throwable) {
        logger.error("", throwable);
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {
        logger.debug("[WebSocket] on BinaryMessage ...oops currently not supported");
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void onWebSocketText(String s) {
        logger.debug("[WebSocket] on TextMessage");
        Message message = Message.sign(
                getChannelConf().getIdentity(),
                Collections.singleton(getChannel()),
                s);
        station.broadcast(message);
    }

    // ===================================================================================
    //                                                                          Receiver
    //                                                                          ==========
    @Override
    public void receive(Message message) {
        logger.debug("Channel on receive\nMessage:\n{}", message);

        ArgumentMeta messageMeta = null;
        for (ArgumentMeta meta : onReceive.getArgumentInfo()) {
            if (!meta.isResolved() && Message.class.isAssignableFrom(meta.getType())) {
                messageMeta = new ArgumentMeta(meta.getParameter(), message);
                break;
            }
        }
        try {
            Object parsedMessage;
            if (messageMeta != null) {
                parsedMessage = onReceive.executeWith(messageMeta);
            } else {
                parsedMessage = onReceive.execute();
            }
            if (parsedMessage instanceof String) {
                session.getRemote().sendString((String) parsedMessage);
            } else {
                String json = objectMapper.writeValueAsString(parsedMessage);
                session.getRemote().sendString(json);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private ChannelConfiguration channelCache;

    @Override
    public Channel getChannel() {
        return getChannelConf().getChannel();
    }

    private ChannelConfiguration getChannelConf() {
        if (channelCache == null) {
            channelCache = call(configurer);
        }
        return channelCache;
    }
}
