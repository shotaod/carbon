package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author ubuntu 2016/10/15.
 */
@Configuration
public class DefaultConfiguration {

	@Component
	public TemplateEngine templateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();

		// load template from classpath
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setCharacterEncoding("utf-8");
		resolver.setTemplateMode("HTML");
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		templateEngine.setTemplateResolver(resolver);

		// manually initialize to prevent run initialize when calling process
		loadForced(templateEngine);

		return templateEngine;
	}

	private void loadForced(TemplateEngine engine) {
		engine.getConfiguration();
	}

}
