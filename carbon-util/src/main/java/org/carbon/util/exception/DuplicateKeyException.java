package org.carbon.util.exception;

/**
 * @author Shota Oda 2017/03/06.
 */
public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(String message) {
        super(message);
    }
}
