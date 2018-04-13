package org.carbon.web.context.session.key;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * @author garden 2018/03/21.
 */
public class TTLSessionKey implements SessionKey {
    private String key;
    private LocalDateTime expired;

    private TTLSessionKey(LocalDateTime expired) {
        this.expired = expired;
    }

    public TTLSessionKey(Integer time, TemporalUnit unit, String key) {
        this(LocalDateTime.now().plus(time, unit));
        this.key = key;
    }

    public TTLSessionKey(String key) {
        this(1, ChronoUnit.HOURS, key);
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public boolean expired() {
        return LocalDateTime.now().isAfter(expired);
    }
}
