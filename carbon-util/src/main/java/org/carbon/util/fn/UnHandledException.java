package org.carbon.util.fn;

/**
 * @author Shota Oda 2017/12/31.
 */
public class UnHandledException extends RuntimeException {
    public UnHandledException(Throwable cause) {
        super(cause);
    }
}
