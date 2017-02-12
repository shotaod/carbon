package org.carbon.web.ws;

import com.google.common.base.Objects;

/**
 * @author Shota Oda 2017/01/02.
 */
public class Channel {
    private String id;

    public Channel(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Channel{id=" + id + "}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Channel)) return false;
        Channel channel = (Channel) object;
        return Objects.equal(id, channel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
