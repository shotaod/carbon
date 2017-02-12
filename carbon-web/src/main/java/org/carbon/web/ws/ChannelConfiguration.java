package org.carbon.web.ws;

/**
 * @author Shota Oda 2017/01/02.
 */
public class ChannelConfiguration {
    private Channel channel;
    private Identity identity;

    public static ChannelConfiguration simple(String to, String from) {
        Channel channel = new Channel(to);
        Identity identity = new Identity(from);
        return new ChannelConfiguration(channel, identity);
    }

    public ChannelConfiguration(Channel channel, Identity identity) {
        this.channel = channel;
        this.identity = identity;
    }

    public Channel getChannel() {
        return channel;
    }

    public Identity getIdentity() {
        return identity;
    }
}
