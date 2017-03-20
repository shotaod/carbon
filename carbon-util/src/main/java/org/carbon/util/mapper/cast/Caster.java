package org.carbon.util.mapper.cast;

/**
 * @author Shota Oda 2017/03/20.
 */
public interface Caster<FROM, TO> {
    TO cast(FROM source);
}

