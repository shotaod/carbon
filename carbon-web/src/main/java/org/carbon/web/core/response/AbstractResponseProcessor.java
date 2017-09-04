package org.carbon.web.core.response;

import javax.servlet.http.HttpServletResponse;

import org.carbon.web.exception.ResponseProcessException;

/**
 * @author Shota Oda 2016/10/14.
 */
public abstract class AbstractResponseProcessor<THIS extends AbstractResponseProcessor, DATA> implements ResponseProcessor {
    protected boolean initialized = false;

    public THIS init(DATA data) {
        THIS self = doInit(data);
        self.initialized = true;
        return self;
    }

    @Override
    public boolean process(HttpServletResponse response) {
        try {
            if (!initialized) {
                throw new IllegalStateException("processor not initialized");
            }
            return doProcess(response);
        } catch (Exception ex) {
            throw new ResponseProcessException(ex);
        }
    }

    abstract protected THIS doInit(DATA data);
    abstract protected boolean doProcess(HttpServletResponse response) throws Exception;
}
