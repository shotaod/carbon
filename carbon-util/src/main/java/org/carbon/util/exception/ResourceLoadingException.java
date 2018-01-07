package org.carbon.util.exception;

/**
 * @author Shota Oda 2018/01/01.
 */
public class ResourceLoadingException extends RuntimeException {
    public ResourceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
