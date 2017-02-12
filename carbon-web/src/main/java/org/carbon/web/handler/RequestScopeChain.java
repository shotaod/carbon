package org.carbon.web.handler;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.context.ApplicationPool;
import org.carbon.web.context.RequestContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class RequestScopeChain extends HttpScopeChain {

    @Inject
    private RequestContainer requestPool;

    @Override
    protected void in(HttpServletRequest request, HttpServletResponse response) {
        requestPool.setObject(response, HttpServletResponse.class);
        requestPool.setObject(request, HttpServletRequest.class);
    }

    @Override
    protected void out(HttpServletRequest request, HttpServletResponse response) {
        requestPool.clear();
    }
}
