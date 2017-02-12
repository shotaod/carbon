package org.carbon.web.core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Component;
import org.carbon.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class JsonKeyValueRequestMapper implements TypeSafeRequestMapper {

    @Override
    public <T> T map(HttpServletRequest request, Class<T> mapTo) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(request.getReader(), new TypeReference<T>(){});
        } catch (IOException e) {
            throw new RequestMappingException("json mapping exception", e);
        }
    }
}
