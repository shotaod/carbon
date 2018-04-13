package org.carbon.web.translate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.translate.dto.Html;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class HtmlTranslator implements HttpTranslator<Html> {

    @Assemble
    private TemplateEngineExecutor templateEngine;

    @Override
    public Class<Html> targetType() {
        return Html.class;
    }

    @Override
    public void translate(HttpServletRequest request, HttpServletResponse response, Html html) throws Throwable {
        response.setContentType("text/html; charset=utf-8");
        templateEngine.run(html, response);
    }
}
