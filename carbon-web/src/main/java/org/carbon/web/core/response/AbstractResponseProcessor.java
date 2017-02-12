package org.carbon.web.core.response;

/**
 * @author Shota Oda 2016/10/14.
 */
public abstract class AbstractResponseProcessor implements ResponseProcessor {
    protected Object result;

    public AbstractResponseProcessor with(Object result) {
        this.result = result;
        return this;
    }
}
