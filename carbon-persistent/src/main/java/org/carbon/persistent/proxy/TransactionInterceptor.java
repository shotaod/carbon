package org.carbon.persistent.proxy;

import net.sf.cglib.proxy.MethodProxy;
import org.carbon.component.construct.proxy.ProxyAdapter;
import org.carbon.persistent.annotation.Transactional;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Shota Oda 2016/11/25.
 */
@Component
public class TransactionInterceptor implements ProxyAdapter {
    @Inject
    private DataSource dataSource;

    @Override
    public boolean shouldHandle(Class clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .anyMatch(method -> method.isAnnotationPresent(Transactional.class));
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        boolean shouldProxy = method.isAnnotationPresent(Transactional.class);

        if (shouldProxy) {
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            Object result;
            try {
                result = methodProxy.invokeSuper(obj, args);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
            conn.commit();
            return result;
        }

        return methodProxy.invokeSuper(obj, args);
    }
}
