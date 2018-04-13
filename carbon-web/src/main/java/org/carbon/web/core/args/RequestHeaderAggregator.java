package org.carbon.web.core.args;

import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.util.mapper.KeyValueMapper;
import org.carbon.web.annotation.RequestHeader;
import org.carbon.web.container.ArgumentMeta;

/**
 * @author Shota.Oda 2018/02/22.
 */
@Component
public class RequestHeaderAggregator implements ArgumentAggregator<RequestHeader> {
    @Assemble
    private KeyValueMapper keyValueMapper;

    @Override
    public Class<RequestHeader> target() {
        return RequestHeader.class;
    }

    @Override
    public ArgumentMeta aggregate(Parameter parameter, HttpServletRequest request) {
        String key = parameter.getDeclaredAnnotation(RequestHeader.class).value();
        Class<?> mapTo = parameter.getType();
        Object value;
        if (key.isEmpty()) {
            Enumeration<String> headers = request.getHeaderNames();
            Map<String, Object> headerMap = new HashMap<>();
            while (headers.hasMoreElements()) {
                String headerName = headers.nextElement();
                headerMap.put(headerName, request.getHeader(headerName));
            }
            value = keyValueMapper.mapAndConstruct(headerMap, mapTo);
        } else {
            String headerValue = request.getHeader(key);
            value = keyValueMapper.mapPrimitive(headerValue, mapTo);
        }
        return new ArgumentMeta(parameter, value);
    }
}
