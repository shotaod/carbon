package org.carbon.web.core.request;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Component;
import org.carbon.web.exception.RequestMappingException;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class JsonKeyValueRequestMapper implements TypeSafeRequestMapper {

    private ObjectMapper objectMapper;

    public JsonKeyValueRequestMapper() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public <T> T map(HttpServletRequest request, Class<T> mapTo) {

        try {
            return objectMapper.readValue(request.getReader(), mapTo);
        } catch (IOException e) {
            throw new RequestMappingException("json mapping exception", e);
        }
    }
}
