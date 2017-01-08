package org.carbon.web.ws;

/**
 * @author ubuntu 2017/01/02.
 */
public interface Receiver {
    void receive(Message message);
    String id();
    Channel getChannel();
}
