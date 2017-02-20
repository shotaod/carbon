package org.carbon.web.core.response.template;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.context.RequestContainer;
import org.carbon.web.core.response.HtmlResponse;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class TemplateEngineWrapper {

    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private RequestContainer requestPool;

    public void run(HtmlResponse source, HttpServletResponse response) throws IOException{
        ContextHandler.Context context = WebAppContext.getCurrentContext();
        HttpServletRequest request = requestPool.getByType(HttpServletRequest.class);

        WebContext webContext = new WebContext(request, response, context.getContext("/"));
        webContext.setVariables(source.getData());
        templateEngine.process(source.getHtmlPath(), webContext, response.getWriter());
    }
}
