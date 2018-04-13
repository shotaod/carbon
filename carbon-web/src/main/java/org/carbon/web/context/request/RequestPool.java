package org.carbon.web.context.request;

import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.web.context.Pool;
import org.carbon.web.context.InstanceContainer;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class RequestPool implements Pool {
    private RequestPool() {
    }

    private static ThreadLocal<InstanceContainer> threadLocal = ThreadLocal.withInitial(() -> new InstanceContainer(new HashMap<>()));

    public void setObject(Object object, Class typeAs) {
        threadLocal.get().set(object, typeAs);
    }

    public void setObject(Object object) {
        threadLocal.get().set(object);
    }

    @Override
    public <T> Optional<T> getByType(Class<T> type) {
        return Optional.ofNullable(threadLocal.get().getByType(type));
    }

    public HttpServletRequest getRequest() {
        return getByType(HttpServletRequest.class).orElseThrow(() -> new IllegalStateException("HttpServletRequest.class not found.Should use RequestScopeChain"));
    }

    public HttpServletResponse getResponse() {
        return getByType(HttpServletResponse.class).orElseThrow(() -> new IllegalStateException("HttpServletResponse.class not found.Should use RequestScopeChain"));
    }

    public void clear() {
        threadLocal.remove();
    }
}
