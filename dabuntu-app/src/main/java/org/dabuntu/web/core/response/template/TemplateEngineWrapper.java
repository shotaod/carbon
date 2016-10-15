package org.dabuntu.web.core.response.template;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.context.RequestContainer;
import org.dabuntu.web.core.response.HtmlResponse;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO to be interface
 * @author ubuntu 2016/10/14.
 */
@Component
public class TemplateEngineWrapper {

	private TemplateEngine cachedEngine;

	public void run(HtmlResponse source, HttpServletResponse response) throws IOException{
		ContextHandler.Context context = WebAppContext.getCurrentContext();
		RequestContainer requestPool = ApplicationPool.instance.getRequestPool();
		HttpServletRequest request = requestPool.getByType(HttpServletRequest.class);

		WebContext webContext = new WebContext(request, response, context.getContext("/"));
		webContext.setVariables(source.getData());
		this.getEngine().process(source.getHtmlPath(), webContext, response.getWriter());
	}

	private TemplateEngine getEngine() {
		if (this.cachedEngine != null) {
			return this.cachedEngine;
		}

		TemplateEngine templateEngine = new TemplateEngine();
		// load template from classpath
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setTemplateMode("XHTML");
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		templateEngine.setTemplateResolver(resolver);

		this.cachedEngine = templateEngine;

		return templateEngine;
	}
}
