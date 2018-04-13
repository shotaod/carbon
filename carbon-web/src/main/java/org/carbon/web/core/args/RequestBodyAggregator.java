package org.carbon.web.core.args;

import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.annotation.RequestBody;
import org.carbon.web.container.ArgumentMeta;
import org.carbon.web.core.request.RequestBodyMapper;

/**
 * @author Shota.Oda 2018/02/22.
 */
@Component
public class RequestBodyAggregator implements ArgumentAggregator<RequestBody> {
    @Assemble
    private RequestBodyMapper requestBodyMapper;

    @Override
    public Class<RequestBody> target() {
        return RequestBody.class;
    }

    @Override
    public ArgumentMeta aggregate(Parameter parameter, HttpServletRequest request) {
        Object body = requestBodyMapper.map(request, parameter.getType());
        return new ArgumentMeta(parameter, body);
    }
}
