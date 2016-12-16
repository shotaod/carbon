package org.carbon.web.core.response;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.ActionResult;
import org.carbon.web.core.response.processor.HtmlProcessor;
import org.carbon.web.core.response.processor.HttpOperationProcessor;
import org.carbon.web.core.response.processor.JsonProcessor;

/**
 * @author ubuntu 2016/10/14.
 */
@Component
public class ResponseProcessorFactory {
	@Inject
	private JsonProcessor jsonProcessor;
	@Inject
	private HtmlProcessor htmlProcessor;
	@Inject
	private HttpOperationProcessor httpOperationProcessor;

	public ResponseProcessor from(ActionResult actionResult) {
		Object result = actionResult.getResult();
		if (result instanceof HtmlResponse) {
			HtmlResponse response = (HtmlResponse) result;
			return htmlProcessor.with(response);
		} else if (result instanceof HttpOperation) {
			HttpOperation response = (HttpOperation) result;
			return httpOperationProcessor.with(response);
		}

		return jsonProcessor.with(result);
	}
}
