package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.core.response.ResponseProcessorFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/05.
 */
@Component
public class ActionFinisher {

	@Inject
	private ResponseProcessorFactory processorFactory;

	public boolean finish(HttpServletResponse response, ActionResult result) {
		return processorFactory.from(result).process(response);
	}
}
