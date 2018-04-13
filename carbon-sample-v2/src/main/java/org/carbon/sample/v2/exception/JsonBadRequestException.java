package org.carbon.sample.v2.exception;

/**
 * @author Shota.Oda 2018/02/23.
 */
public class JsonBadRequestException extends Exception {

    public JsonBadRequestException(String message) {
        super(message);
    }

    public JsonBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
