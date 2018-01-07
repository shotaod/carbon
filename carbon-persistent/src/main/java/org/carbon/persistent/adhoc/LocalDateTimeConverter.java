package org.carbon.persistent.adhoc;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.Converter;

/**
 * @author Shota Oda 2016/12/11.
 */
public class LocalDateTimeConverter implements Converter<Timestamp, LocalDateTime> {
    @Override
    public LocalDateTime from(Timestamp databaseObject) {
        if (databaseObject == null) {
            return null;
        }
        return databaseObject.toLocalDateTime();
    }

    @Override
    public Timestamp to(LocalDateTime userObject) {
        if (userObject == null) {
            return null;
        }
        return Timestamp.valueOf(userObject);
    }

    @Override
    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public Class<LocalDateTime> toType() {
        return LocalDateTime.class;
    }
}
