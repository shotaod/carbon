package org.dabuntu.web.core.response.processor;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.web.core.response.AbstractResponseProcessor;
import org.dabuntu.web.core.response.HttpOperation;
import org.dabuntu.web.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/11/28.
 */
@Component
public class HttpOperationProcessor extends AbstractResponseProcessor {

	private HttpOperation httpOperation;

	public HttpOperationProcessor with(HttpOperation httpOperation) {
		this.httpOperation = httpOperation;
		return this;
	}

	@Override
	public boolean process(HttpServletResponse response) {
		switch (httpOperation.getStrategy()) {
			case Forward:
			case Redirect:
				ResponseUtil.redirect(response, httpOperation.getPathTo());
		}
		return true;
	}
}
