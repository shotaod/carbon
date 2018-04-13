package org.carbon.sample.v2.app.api.rocketty.ranking;

import org.carbon.sample.v2.exception.JsonBadRequestException;

/**
 * @author garden 2018/03/25.
 */
public class UserNotFoundException extends JsonBadRequestException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
