package org.carbon.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.web.container.ActionResult;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.core.ActionAggregate;

/**
 * @author Shota Oda 2016/10/17.
 */
@Component
public class CoreDispatchChain extends HandlerChain {
    @Assemble
    private ActionAggregate core;
    @Assemble
    private RequestPool requestContext;

    @Override
    protected void chain(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        super.chain(request, response);
        ActionResult result = core.execute(request);
        requestContext.setObject(result);
    }
}
