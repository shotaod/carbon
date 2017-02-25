package org.carbon.web.core.response;

import javax.servlet.http.HttpServletResponse;

import org.carbon.web.exception.ResponseProcessException;

/**
 * @author Shota Oda 2016/10/14.
 */
public abstract class AbstractResponseProcessor implements ResponseProcessor {
    @Override
    public boolean process(HttpServletResponse response) {
        try {
            return doProcess(response);
        } catch (Exception ex) {
            throw new ResponseProcessException(ex);
        }
    }

    abstract protected boolean doProcess(HttpServletResponse response) throws Exception;
}
