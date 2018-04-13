package org.carbon.persistent.adhoc;

import org.jooq.Converter;

/**
 * @author garden 2018/03/25.
 */
public class BooleanConverter implements Converter<Byte, Boolean> {

    private byte FALSE = 0;
    private byte TRUE = 1;

    @Override
    public Boolean from(Byte databaseObject) {
        if (databaseObject == null) {
            return null;
        }
        return databaseObject == TRUE;
    }

    @Override
    public Byte to(Boolean userObject) {
        if (userObject == null) {
            return null;
        }
        return userObject ? TRUE : FALSE;
    }

    @Override
    public Class<Byte> fromType() {
        return Byte.class;
    }

    @Override
    public Class<Boolean> toType() {
        return Boolean.class;
    }
}
