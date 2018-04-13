package org.carbon.web.core.request;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.exception.request_mapping.UnsupportedRequestException;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class RequestBodyMapper {
    @Assemble
    private RequestMapperFactory factory;

    public <T> T map(HttpServletRequest request, Class<T> mapTo) {
        ContentType contentType = Optional.ofNullable(request.getHeader("content-type"))
                .map(ContentType::new)
                .orElse(null);
        if (contentType == null) {
            return null;
        }
        return factory.factorize(contentType)
                .map(mapper -> mapper.map(request, mapTo))
                .orElseThrow(() -> new UnsupportedRequestException("content-type not supported: " + contentType));
    }
}
