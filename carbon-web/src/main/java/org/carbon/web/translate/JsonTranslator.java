package org.carbon.web.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.translate.dto.Json;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class JsonTranslator implements HttpTranslator<Json> {

    @Assemble
    private ObjectMapper mapper;

    @Override
    public Class<Json> targetType() {
        return Json.class;
    }

    @Override
    public void translate(HttpServletRequest request, HttpServletResponse response, Json json) throws Throwable {
        response.setContentType("application/json; charset=utf-8");
        mapper.writeValue(response.getWriter(), json);
    }
}
