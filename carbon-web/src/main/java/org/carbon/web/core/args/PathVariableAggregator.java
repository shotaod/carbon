package org.carbon.web.core.args;

import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.mapper.KeyValueMapper;
import org.carbon.web.annotation.PathVariable;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.container.PathVariables;
import org.carbon.web.context.request.RequestPool;

/**
 * @author Shota.Oda 2018/02/22.
 */
@Component
public class PathVariableAggregator implements ArgumentAggregator<PathVariable> {
    @Assemble
    private RequestPool requestPool;
    @Assemble
    private KeyValueMapper keyValueMapper;

    @Override
    public Class<PathVariable> target() {
        return PathVariable.class;
    }

    @Override
    public ArgumentMeta aggregate(Parameter parameter, HttpServletRequest request) {
        String variableName = parameter.getDeclaredAnnotation(PathVariable.class).value();
        Object value = requestPool.getByType(PathVariables.class)
                .map(v -> v.getValue(variableName))
                .map(v -> keyValueMapper.mapPrimitive(v, parameter.getType()))
                .orElseThrow(() -> new IllegalStateException(""));
        return new ArgumentMeta(parameter, value);
    }
}
