package org.carbon.web.core.response.processor;

import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.web.core.response.AbstractResponseProcessor;
import org.carbon.web.core.response.HttpOperation;
import org.carbon.web.util.ResponseUtil;

/**
 * @author Shota Oda 2016/11/28.
 */
@Component
public class HttpOperationProcessor extends AbstractResponseProcessor<HttpOperationProcessor, HttpOperation> {

    private HttpOperation httpOperation;

    @Override
    public HttpOperationProcessor doInit(HttpOperation httpOperation) {
        HttpOperationProcessor self = new HttpOperationProcessor();
        self.httpOperation = httpOperation;
        return self;
    }

    @Override
    public boolean doProcess(HttpServletResponse response) throws Exception {
        switch (httpOperation.getStrategy()) {
            case Forward:
            case Redirect:
                ResponseUtil.redirect(response, httpOperation.getPathTo());
        }
        return true;
    }
}
