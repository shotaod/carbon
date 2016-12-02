package org.dabunt.persistent.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.dabunt.persistent.annotation.Transactional;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * @author ubuntu 2016/11/25.
 */
@Component
public class TransactionInterceptor implements MethodInterceptor {
	@Inject
	DataSource dataSource;
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
				return null;
			}
			conn.commit();
			return result;
		}

		return methodProxy.invokeSuper(obj, args);
	}
}
