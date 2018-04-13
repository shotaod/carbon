package org.carbon.web.ws;

/**
 * @author Shota.Oda 2018/02/24.
 */
public abstract class AbstractReceiver implements Receiver {
    protected String id;

    @Override
    public String id() {
        return id;
    }

    @Override
    public void assignId(String id) {
        this.id = id;
    }
}
