package org.carbon.sample.heroku.util;

/**
 * @author Shota Oda 2017/08/10.
 */
public class Tuple<T1, T2> {
    private T1 _val1;
    private T2 _val2;

    public Tuple(T1 _val1, T2 _val2) {
        this._val1 = _val1;
        this._val2 = _val2;
    }

    public T1 get_val1() {
        return _val1;
    }

    public T2 get_val2() {
        return _val2;
    }
}
