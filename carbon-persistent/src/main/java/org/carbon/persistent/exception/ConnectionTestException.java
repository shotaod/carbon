package org.carbon.persistent.exception;

/**
 * @author ubuntu 2017/03/04.
 */
public class ConnectionTestException extends RuntimeException {
    public ConnectionTestException(Throwable cause) {
        super("Fail to connect datasource", cause);
    }
}
