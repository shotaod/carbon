package org.carbon.sample.v2.app.api.rocketty.ranking;

import org.carbon.sample.v2.exception.JsonBadRequestException;

/**
 * @author garden 2018/04/01.
 */
public class IllegalRequestException extends JsonBadRequestException {
    public IllegalRequestException(String message) {
        super(message);
    }

    public IllegalRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
