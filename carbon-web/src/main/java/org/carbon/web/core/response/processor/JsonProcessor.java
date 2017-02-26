package org.carbon.web.core.response.processor;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Component;
import org.carbon.web.core.response.AbstractResponseProcessor;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class JsonProcessor extends AbstractResponseProcessor {
    private ObjectMapper mapper = new ObjectMapper();
    private Object object;

    public JsonProcessor with(Object object) {
        this.object = object;
        return this;
    }

    @Override
    public boolean doProcess(HttpServletResponse response) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        mapper.writeValue(response.getWriter(), this.object);
        response.setStatus(HttpServletResponse.SC_OK);
        return true;
    }
}
