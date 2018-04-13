package org.carbon.web.translate.decorate;

import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.web.header.HttpHeaderRegistry;

/**
 * @author Shota.Oda 2018/02/18.
 */
@Component
public class XHttpHeaderDecorator implements HttpDecorator {
    @Assemble
    private HttpHeaderRegistry headerRegistry;

    @Override
    public void decorate(HttpServletResponse response) {
        headerRegistry.getHttpHeaders()
                .forEach(header -> response.setHeader(header.getKey(), header.getValue()));
    }
}
