package org.carbon.web.exception.request_mapping;

import org.carbon.web.exception.WrappedException;

/**
 * @author Shota.Oda 2018/03/03.
 */
public class MultipartFormRequestMappingException extends RequestMappingException {
    public MultipartFormRequestMappingException(Throwable cause) {
        super(cause);
    }

    @Override
    protected WrappedException self() {
        return this;
    }
}
