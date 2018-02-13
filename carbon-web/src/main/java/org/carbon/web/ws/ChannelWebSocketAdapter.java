package org.carbon.web.ws;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.container.ExecutableAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2017/01/07.
 */
public class ChannelWebSocketAdapter implements ChanneledSocket {
    private Logger logger = LoggerFactory.getLogger(ChannelWebSocketAdapter.class);
    private ChannelStation station;
    private ObjectMapper objectMapper;
    private ExecutableAction<Void> onOpen;
    private ExecutableAction<Void> onClose;
    private ExecutableAction<Object> onReceive;
    private ExecutableAction<ChannelConfiguration> onConfig;

    public ChannelWebSocketAdapter(ChannelStation station, ObjectMapper objectMapper) {
        this.station = station;
        this.objectMapper = objectMapper;
    }

    public void setOnOpen(ExecutableAction<Void> onOpen) {
        this.onOpen = onOpen;
    }

    public void setOnClose(ExecutableAction<Void> onClose) {
        this.onClose = onClose;
    }

    public void setOnReceive(ExecutableAction<Object> onReceive) {
        this.onReceive = onReceive;
    }

    public void setOnConfig(ExecutableAction<ChannelConfiguration> onConfig) {
        this.onConfig = onConfig;
    }

    // ===================================================================================
    //                                                                          ChanneledSocket Implementation
    //                                                                          ==========
    private Connection connection;

    @Override
    public void receive(Message message) {
        logger.debug("Channel on receive\nMessage:\n{}", message);
        Map<String, ArgumentMeta> args = onReceive.getArgumentInfo().entrySet().stream().map(entry -> {
            ArgumentMeta meta = entry.getValue();
            if (!meta.isResolved() && Message.class.isAssignableFrom(meta.getType())) {
                ArgumentMeta newMeta = new ArgumentMeta(meta.getParameter(), message);
                return new AbstractMap.SimpleEntry<>(entry.getKey(), newMeta);
            }
            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        try {
            Object parsedMessage = onReceive.executeWith(args);
            if (parsedMessage instanceof String) {
                connection.sendMessage((String)parsedMessage);
            } else {
                String json = objectMapper.writeValueAsString(parsedMessage);
                connection.sendMessage(json);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private ChannelConfiguration channelCache;
    private String id;

    @Override
    public String id() {
        return id;
    }

    @Override
    public Channel getChannel() {
        return getChannelConf().getChannel();
    }

    private ChannelConfiguration getChannelConf() {
        if (channelCache == null) {
            channelCache = onConfig.execute();
        }
        return channelCache;
    }

    @Override
    public void onMessage(byte[] data, int offset, int length) {
        logger.debug("[WebSocket] on BinaryMessage ...oops currently not supported");
        // TODO impl
    }

    @Override
    public void onMessage(String data) {
        logger.debug("[WebSocket] on TextMessage");
        station.broadcast(new Message(getChannelConf().getIdentity(), Collections.singleton(getChannel()), data));
    }

    @Override
    public void onOpen(Connection connection) {
        logger.debug("[WebSocket] Connection Open -> \nconnection:\n{}", connection);
        this.connection = connection;
        this.id = station.register(this);
        onOpen.execute();
    }

    @Override
    public void onClose(int closeCode, String message) {
        logger.debug("[WebSocket] Connection Close -> \ncode: {}\nmessage: {}", closeCode, message);
        station.unregister(this);
        onClose.execute();
    }
}
