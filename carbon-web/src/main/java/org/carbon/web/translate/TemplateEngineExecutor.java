package org.carbon.web.translate;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.translate.dto.Html;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class TemplateEngineExecutor {

    @Assemble
    private ITemplateEngine templateEngine;
    @Assemble
    private RequestPool requestPool;

    public void run(Html source, HttpServletResponse response) throws IOException {
        ContextHandler.Context context = WebAppContext.getCurrentContext();
        HttpServletRequest request = requestPool.getRequest();

        WebContext webContext = new WebContext(request, response, context.getContext("/"));
        webContext.setVariables(source.getData());
        templateEngine.process(source.getHtmlPath(), webContext, response.getWriter());
    }
}
