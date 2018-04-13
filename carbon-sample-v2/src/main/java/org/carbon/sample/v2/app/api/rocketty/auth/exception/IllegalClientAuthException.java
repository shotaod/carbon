package org.carbon.sample.v2.app.api.rocketty.auth.exception;

import org.carbon.sample.v2.exception.JsonBadRequestException;

/**
 * @author Shota.Oda 2018/02/15.
 */
public class IllegalClientAuthException extends JsonBadRequestException {

    public IllegalClientAuthException(String message) {
        super(message);
    }
}
