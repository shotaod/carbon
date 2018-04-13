package org.carbon.web.handler.scope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.web.context.request.RequestPool;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class RequestScopeChain extends ScopeChain {

    @Assemble
    private RequestPool requestContext;

    @Override
    protected void in(HttpServletRequest request, HttpServletResponse response) {
        requestContext.setObject(response, HttpServletResponse.class);
        requestContext.setObject(request, HttpServletRequest.class);
    }

    @Override
    protected void out(HttpServletRequest request, HttpServletResponse response) {
        requestContext.clear();
    }
}
