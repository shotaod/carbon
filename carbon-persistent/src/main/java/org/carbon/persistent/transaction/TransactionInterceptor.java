package org.carbon.persistent.transaction;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.sf.cglib.proxy.MethodProxy;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.enhance.ProxyAdapter;
import org.carbon.persistent.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/11/25.
 */
@Component
public class TransactionInterceptor implements ProxyAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TransactionInterceptor.class);

    @Assemble
    private TransactionManager transactionManager;

    @Override
    public boolean shouldHandle(Class clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(Transactional.class));
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        boolean shouldProxy = method.isAnnotationPresent(Transactional.class);
        if (!shouldProxy) {
            return methodProxy.invokeSuper(obj, args);
        }
        Object result;
        try {
            // begin
            transactionManager.begin();
            logger.debug("[Intercept] transaction begin");
            // statement
            result = methodProxy.invokeSuper(obj, args);
            // commit
            transactionManager.commit();
            logger.debug("[Intercept] transaction committed");
            return result;
        } catch (Throwable e) {
            transactionManager.rollback();
            logger.debug("[Intercept] transaction rollback caz {}", e.getMessage());
            throw e;
        } finally {
            transactionManager.end();
        }
    }
}
