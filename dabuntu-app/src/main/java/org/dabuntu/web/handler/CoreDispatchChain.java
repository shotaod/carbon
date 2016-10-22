package org.dabuntu.web.handler;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.core.DabuntCore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ubuntu 2016/10/17.
 */
@Component
public class CoreDispatchChain extends HttpHandlerChain {
	@Inject
	private DabuntCore core;

	@Override
	protected void chain(HttpServletRequest request, HttpServletResponse response) {
		core.execute(request, response);
		super.chain(request, response);
	}
}
