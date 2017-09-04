package org.carbon.web.core.response.processor;

import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.core.response.AbstractResponseProcessor;
import org.carbon.web.core.response.HtmlResponse;
import org.carbon.web.core.response.template.TemplateEngineWrapper;

/**
 * @author Shota Oda 2016/10/14.
 */
@Component
public class HtmlProcessor extends AbstractResponseProcessor<HtmlProcessor, HtmlResponse> {

    @Inject
    private TemplateEngineWrapper templateEngine;

    private HtmlResponse htmlResponse;

    @Override
    protected HtmlProcessor doInit(HtmlResponse htmlResponse) {
        HtmlProcessor self = new HtmlProcessor();
        self.templateEngine = templateEngine;
        self.htmlResponse = htmlResponse;
        return self;
    }

    @Override
    public boolean doProcess(HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        templateEngine.run(this.htmlResponse, response);
        response.setStatus(HttpServletResponse.SC_OK);
        return true;
    }
}
