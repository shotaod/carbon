package org.carbon.web.core.args;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.mapper.KeyValueMapper;
import org.carbon.web.annotation.RequestParam;
import org.carbon.web.container.ArgumentMeta;

/**
 * @author Shota.Oda 2018/02/22.
 */
@Component
public class RequestParameterAggregator implements ArgumentAggregator<RequestParam> {
    @Assemble
    private KeyValueMapper keyValueMapper;

    @Override
    public Class<RequestParam> target() {
        return RequestParam.class;
    }

    @Override
    public ArgumentMeta aggregate(Parameter parameter, HttpServletRequest request) {
        String key = parameter.getDeclaredAnnotation(RequestParam.class).value();
        Class<?> mapTo = parameter.getType();
        Object value;
        if (key.isEmpty()) {
            Map<String, Object> map = request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> mapParamValue(e.getValue())
                    ));
            value = keyValueMapper.mapAndConstruct(map, mapTo);
        } else if (List.class.isAssignableFrom(mapTo)) {
            String[] values = request.getParameterValues(key);
            value = Arrays.asList(values);
        } else {
            value = request.getParameter(key);
        }

        return new ArgumentMeta(parameter, value);
    }

    private Object mapParamValue(String... params) {
        if (params.length == 0) {
            return null;
        }
        if (params.length == 1) {
            return params[0];
        }
        return params;
    }
}
