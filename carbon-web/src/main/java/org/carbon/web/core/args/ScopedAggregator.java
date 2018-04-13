package org.carbon.web.core.args;

import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.annotation.AnnotationUtil;
import org.carbon.util.mapper.KeyValueMapper;
import org.carbon.web.annotation.scope.AppScope;
import org.carbon.web.annotation.scope.RequestScope;
import org.carbon.web.annotation.scope.Scope;
import org.carbon.web.annotation.scope.SessionScope;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.context.app.ApplicationPool;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.context.session.SessionPool;

/**
 * @author Shota.Oda 2018/02/22.
 */
@Component
public class ScopedAggregator implements ArgumentAggregator<Scope> {
    private ApplicationPool applicationPool = ApplicationPool.instance;
    @Assemble
    private SessionPool sessionPool;
    @Assemble
    private RequestPool requestPool;

    @Assemble
    private KeyValueMapper keyValueMapper;

    @Override
    public Class<Scope> target() {
        return Scope.class;
    }

    @Override
    public boolean handle(Parameter parameter) {
        return AnnotationUtil.isAnnotated(parameter, target());
    }

    @Override
    public ArgumentMeta aggregate(Parameter parameter, HttpServletRequest request) {
        Class<?> type = parameter.getType();
        Object value = null;
        if (AnnotationUtil.isAnnotated(parameter, AppScope.class)) {
            value = applicationPool.getByType(type).orElse(null);
        } else if (AnnotationUtil.isAnnotated(parameter, SessionScope.class)) {
            value = sessionPool.getByType(type).orElse(null);
        } else if (AnnotationUtil.isAnnotated(parameter, RequestScope.class)) {
            value = requestPool.getByType(type).orElse(null);
        }
        return new ArgumentMeta(parameter, value);
    }
}
