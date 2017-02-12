package org.carbon.web.exception;

/**
 * @author Shota Oda 2016/11/03.
 */
public class AuthStrategyNotRegisteredException extends RuntimeException {
    public AuthStrategyNotRegisteredException(String message) {
        super(message);
    }
}
