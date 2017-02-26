package org.carbon.web.ws;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.util.format.StringLineBuilder;

/**
 * @author Shota Oda 2017/01/02.
 */
public class Message {
    private Identity from;
    private Set<Channel> channels;
    private String content;

    public Message(Identity from, Set<Channel> channels, String content) {
        this.from = from;
        this.channels = channels;
        this.content = content;
    }

    public Identity getFrom() {
        return from;
    }
    public Set<Channel> getChannels() {
        return channels;
    }
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringLineBuilder sb = new StringLineBuilder();
        List<SimpleKeyValue<String, ?>> values = Arrays.asList(
                new SimpleKeyValue<>("from", from),
                new SimpleKeyValue<>("to", channels.stream().map(Channel::toString).collect(Collectors.joining(","))),
                new SimpleKeyValue<>("content", content)
        );
        sb.appendLine(BoxedTitleMessage.produceLeft(values));
        return sb.toString();
    }
}

