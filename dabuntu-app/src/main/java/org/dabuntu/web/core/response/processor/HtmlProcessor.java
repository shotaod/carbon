package org.dabuntu.web.core.response.processor;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.core.response.AbstractResponseProcessor;
import org.dabuntu.web.core.response.HtmlResponse;
import org.dabuntu.web.core.response.template.TemplateEngineWrapper;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/14.
 */
@Component
public class HtmlProcessor extends AbstractResponseProcessor {

	@Inject
	private TemplateEngineWrapper templateEngine;

	private HtmlResponse htmlResponse;

	public HtmlProcessor with(HtmlResponse htmlResponse) {
		this.htmlResponse = htmlResponse;
		return this;
	}

	@Override
	public boolean process(HttpServletResponse response) {
		try {
			templateEngine.run(this.htmlResponse, response);
		} catch (Exception e) {
			return false;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		return true;
	}
}
