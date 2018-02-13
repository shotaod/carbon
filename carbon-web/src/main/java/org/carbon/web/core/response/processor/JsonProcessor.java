package org.carbon.web.core.response.processor;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.core.response.AbstractResponseProcessor;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class JsonProcessor extends AbstractResponseProcessor<JsonProcessor, Object> {

    @Inject
    private ObjectMapper mapper;
    private Object object;

    @Override
    protected JsonProcessor doInit(Object o) {
        JsonProcessor self = new JsonProcessor();
        self.mapper = mapper;
        self.object = o;
        return self;
    }

    @Override
    public boolean doProcess(HttpServletResponse response) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        mapper.writeValue(response.getWriter(), this.object);
        response.setStatus(HttpServletResponse.SC_OK);
        return true;
    }
}
