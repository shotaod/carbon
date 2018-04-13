package org.carbon.web.core.args;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.mapper.KeyValueMapper;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.container.ArgumentMetas;
import org.carbon.web.container.ExecutableAction;
import org.carbon.web.context.app.ApplicationPool;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.context.session.SessionPool;
import org.carbon.web.core.InstanceScope;
import org.carbon.web.core.request.RequestBodyMapper;

/**
 * @author Shota Oda 2016/10/11.
 */
@Component
public class ActionArgumentAggregatorFactory {
    private ApplicationPool applicationPool = ApplicationPool.instance;
    @Assemble
    private RequestPool requestPool;
    @Assemble
    private SessionPool sessionPool;

    @Assemble
    private List<ArgumentAggregator> aggregators;
    @Assemble
    private List<ArgumentAggregatorAfter> afterAggregators;
    @Assemble
    private RequestBodyMapper requestBodyMapper;
    @Assemble
    private Validator validator;
    @Assemble
    private KeyValueMapper keyValueMapper;

    public ActionArgumentAggregator newAggregator() {
        return new ActionArgumentAggregator();
    }

    public class ActionArgumentAggregator {

        private ActionArgumentAggregator() {
        }

        public <T> Optional<T> find(Class<T> type, InstanceScope source) {
            return ActionArgumentAggregatorFactory.this.find(type, source);
        }

        public HttpServletRequest getRequest() {
            return requestPool.getRequest();
        }

        public HttpServletResponse getResponse() {
            return requestPool.getResponse();
        }

        public ExecutableAction resolve(Method method, Object instance) {
            return ActionArgumentAggregatorFactory.this.resolve(method, instance);
        }
    }

    private <T> Optional<T> find(Class<T> type, InstanceScope source) {
        switch (source) {
            case Request:
                return requestPool.getByType(type);
            case Session:
                return sessionPool.getByType(type);
            case Application:
                return applicationPool.getByType(type);
            default:
                return Optional.empty();
        }
    }

    private ExecutableAction resolve(Method method, Object instance) {
        HttpServletRequest request = requestPool.getRequest();

        ArgumentMetas metas = new ArgumentMetas();
        if (method.getParameterCount() > 0) {
            Iterator<Parameter> iterator = Stream.of(method.getParameters()).iterator();
            // join another resolved to shouldResolves
            doResolve(iterator.next(), iterator, metas, request);
        }

        return new ExecutableAction<>(instance, method, metas);
    }

    private void doResolve(Parameter current, Iterator<Parameter> iterator, ArgumentMetas resolved, HttpServletRequest request) {
        Parameter next = null;
        if (iterator.hasNext()) {
            next = iterator.next();
        }
        for (ArgumentAggregator aggregator : aggregators) {
            if (aggregator.handle(current)) {
                ArgumentMeta meta = aggregator.aggregate(current, request);
                resolved.putMeta(meta);
                for (ArgumentAggregatorAfter after : afterAggregators) {
                    if (after.handle(current)) {
                        ArgumentMeta afterMeta = after.aggregate(meta, request, next);
                        if (afterMeta != null) resolved.putMeta(afterMeta);
                    }
                }
                break;
            }
        }
        if (next != null) {
            doResolve(next, iterator, resolved, request);
        }
    }
}
