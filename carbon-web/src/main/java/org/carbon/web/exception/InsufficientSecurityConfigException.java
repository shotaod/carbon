package org.carbon.web.exception;

/**
 * @author Shota Oda 2017/02/13.
 */
public class InsufficientSecurityConfigException extends RuntimeException {
    public InsufficientSecurityConfigException(String message) {
        super(message);
    }
}
