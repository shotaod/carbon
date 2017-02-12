package org.carbon.web.core.response.processor;

import org.carbon.web.core.response.AbstractResponseProcessor;
import org.carbon.web.util.ResponseUtil;
import org.carbon.component.annotation.Component;
import org.carbon.web.core.response.HttpOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/11/28.
 */
@Component
public class HttpOperationProcessor extends AbstractResponseProcessor {

    private HttpOperation httpOperation;

    public HttpOperationProcessor with(HttpOperation httpOperation) {
        this.httpOperation = httpOperation;
        return this;
    }

    @Override
    public boolean process(HttpServletResponse response) {
        switch (httpOperation.getStrategy()) {
            case Forward:
            case Redirect:
                ResponseUtil.redirect(response, httpOperation.getPathTo());
        }
        return true;
    }
}
