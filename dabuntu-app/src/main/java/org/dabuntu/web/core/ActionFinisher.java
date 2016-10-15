package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.core.response.ResponseProcessorFactory;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/05.
 */
@Component
public class ActionFinisher {

	@Inject
	private ResponseProcessorFactory processorFactory;

	public void finish(Request baseRequest, HttpServletResponse response, ActionResult result) {
		boolean processed = processorFactory.from(result).process(response);
		baseRequest.setHandled(processed);
	}
}
