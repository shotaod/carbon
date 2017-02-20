package org.carbon.sample.heroku.exception;

/**
 * @author Shota Oda 2017/02/12.
 */
public class SetupException extends RuntimeException {
    public SetupException(String message) {
        super(message);
    }
}
