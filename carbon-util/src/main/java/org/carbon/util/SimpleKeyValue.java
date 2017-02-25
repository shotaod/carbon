package org.carbon.util;

/**
 * @author Shota Oda 2016/10/08.
 */
public class SimpleKeyValue<KEY,VALUE> {
    private KEY key;
    private VALUE value;

    public SimpleKeyValue(KEY key, VALUE value) {
        this.key = key;
        this.value = value;
    }

    public KEY getKey() {
        return key;
    }

    public VALUE getValue() {
        return value;
    }
}
