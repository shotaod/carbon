package org.carbon.web.core;

import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.ActionResult;
import org.carbon.web.core.response.ResponseProcessorFactory;

/**
 * @author Shota Oda 2016/10/05.
 */
@Component
public class ActionFinisher {

    @Inject
    private ResponseProcessorFactory processorFactory;

    public boolean finish(HttpServletResponse response, ActionResult result) {
        if (result.handled()) return true;
        if (result.hasException()) return false;

        return processorFactory.from(result).process(response);
    }
}
