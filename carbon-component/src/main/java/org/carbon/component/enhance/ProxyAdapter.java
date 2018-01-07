package org.carbon.component.enhance;

import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author Shota Oda 2017/02/09.
 */
public interface ProxyAdapter extends MethodInterceptor {
    boolean shouldHandle(Class clazz);
}
