package org.carbon.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.context.request.RequestContext;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class RequestScopeChain extends ScopeChain {

    @Inject
    private RequestContext requestContext;

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
