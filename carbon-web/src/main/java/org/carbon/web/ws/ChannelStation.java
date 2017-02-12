package org.carbon.web.ws;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Shota Oda 2017/01/02.
 */
public class ChannelStation {
    public static ChannelStation instance = new ChannelStation();
    private Map<Channel, Map<String, Receiver>> channels;

    private ChannelStation() {
        channels = new HashMap<>();
    }

    public String register(Receiver receiver) {
        String id = generateId();
        Map<String, Receiver> receivers = channels.computeIfAbsent(receiver.getChannel(), channel -> new HashMap<>());
        receivers.put(id, receiver);

        return id;
    }

    public void unregister(Receiver receiver) {
        Map<String, Receiver> receivers = channels.get(receiver.getChannel());
        if (receivers != null) {
            receivers.remove(receiver.id());
        }
    }

    public void broadcast(Message message) {
        channels.entrySet().stream()
            .filter(entry -> message.getChannels().contains(entry.getKey()))
            .flatMap(entry -> entry.getValue().values().stream())
            .forEach(receiver -> receiver.receive(message));
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}