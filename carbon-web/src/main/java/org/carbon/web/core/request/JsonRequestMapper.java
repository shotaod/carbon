package org.carbon.web.core.request;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.exception.request_mapping.JsonRequestMappingException;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class JsonRequestMapper implements TypeSafeRequestMapper {

    @Assemble
    private ObjectMapper objectMapper;

    @Override
    public <T> T map(HttpServletRequest request, Class<T> mapTo) {

        try {
            return objectMapper.readValue(request.getReader(), mapTo);
        } catch (IOException e) {
            throw new JsonRequestMappingException(e);
        }
    }
}
