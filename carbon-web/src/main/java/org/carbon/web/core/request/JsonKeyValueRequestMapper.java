package org.carbon.web.core.request;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.exception.RequestMappingException;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class JsonKeyValueRequestMapper implements TypeSafeRequestMapper {

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public <T> T map(HttpServletRequest request, Class<T> mapTo) {

        try {
            return objectMapper.readValue(request.getReader(), mapTo);
        } catch (IOException e) {
            throw new RequestMappingException("json mapping exception", e);
        }
    }
}
