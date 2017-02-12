package org.carbon.web.context;

/**
 * @author Shota Oda 2017/01/14.
 */
public interface PoolAware {
    void aware(ApplicationPool pool);
}
