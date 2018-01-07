package org.carbon.sample.v2.web.oauth.repository;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

/**
 * @author Shota Oda 2017/07/30.
 */
public class Expiration {
    protected LocalDateTime expire;

    public Expiration(Integer expire, TemporalUnit unit) {
        this.expire = LocalDateTime.now().plus(expire, unit);
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expire);
    }
}
