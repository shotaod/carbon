package org.carbon.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.header.HttpHeaderRegistry;

/**
 * @author Shota Oda 2017/02/17.
 */
@Component
public class XHttpHeaderChain extends HandlerChain {
    @Inject
    private HttpHeaderRegistry headerRegistry;

    @Override
    protected void chain(HttpServletRequest request, HttpServletResponse response) {
        headerRegistry.getHttpHeaders()
                .forEach(header -> response.setHeader(header.getKey(), header.getValue()));
        super.chain(request, response);
    }
}
