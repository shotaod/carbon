package org.carbon.web.conf;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


/**
 * @author Shota Oda 2016/10/15.
 */
@Configuration
public class DefaultConfiguration {

    @Component
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        // use Layout fragment
        templateEngine.addDialect(new LayoutDialect());
        // use java8
        templateEngine.addDialect(new Java8TimeDialect());

        // load template from classpath
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCharacterEncoding("utf-8");
        resolver.setTemplateMode("HTML");
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setCacheable(false);
        templateEngine.setTemplateResolver(resolver);

        // manually initialize to prevent lazy loading
        loadForced(templateEngine);

        return templateEngine;
    }

    private void loadForced(TemplateEngine engine) {
        engine.getConfiguration();
    }

    @Component
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    @Component
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
