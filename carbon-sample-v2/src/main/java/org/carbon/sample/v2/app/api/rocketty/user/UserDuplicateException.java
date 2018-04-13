package org.carbon.sample.v2.app.api.rocketty.user;

import org.carbon.sample.v2.exception.JsonBadRequestException;

/**
 * @author garden 2018/03/25.
 */
public class UserDuplicateException extends JsonBadRequestException {
    public UserDuplicateException(String message) {
        super(message);
    }
}
