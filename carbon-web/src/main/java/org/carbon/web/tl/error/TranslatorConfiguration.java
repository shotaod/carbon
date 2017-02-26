package org.carbon.web.tl.error;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.web.conf.WebProperty;
import org.carbon.web.core.response.processor.HtmlProcessor;

/**
 * @author Shota Oda 2017/02/19.
 */
@Configuration
public class TranslatorConfiguration {
    @Inject
    private WebProperty webProperty;
    @Inject
    private HtmlProcessor htmlProcessor;

    @Component
    public HttpErrorTranslator translator() {
        MinimumHtmlTranslator translator = new MinimumHtmlTranslator(htmlProcessor);
        WebProperty.CustomError customError = webProperty.getCustomError();
        if (customError != null) {
            translator.setNotFoundPage(customError.getNotFound());
            translator.setInternalServerErrorPage(customError.getServerError());
        }
        return translator;
    }
}
