package org.dabuntu.web.core.response;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.core.response.processor.HtmlProcessor;
import org.dabuntu.web.core.response.processor.JsonProcessor;

/**
 * @author ubuntu 2016/10/14.
 */
@Component
public class ResponseProcessorFactory {
	@Inject
	private JsonProcessor jsonProcessor;
	@Inject
	private HtmlProcessor htmlProcessor;

	public ResponseProcessor from(ActionResult actionResult) {
		Object result = actionResult.getResult();
		if (result instanceof HtmlResponse) {
			HtmlResponse response = (HtmlResponse) result;
			return htmlProcessor.with(response);
		}

		return jsonProcessor.with(result);
	}
}
