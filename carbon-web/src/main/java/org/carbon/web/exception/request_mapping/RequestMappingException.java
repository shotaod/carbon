package org.carbon.web.exception.request_mapping;


import org.carbon.web.exception.WrappedException;

/**
 * @author Shota Oda 2016/10/12.
 */
public abstract class RequestMappingException extends WrappedException {
    public RequestMappingException() {
    }

    public RequestMappingException(Throwable cause) {
        super(cause);
    }

    public RequestMappingException(String message) {
        super(message);
    }

    public RequestMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
