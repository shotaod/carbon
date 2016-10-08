package org.dabuntu.web.handler;

import org.dabuntu.web.core.ActionExecutor;
import org.dabuntu.web.annotation.Default;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.core.ActionResolver;
import org.dabuntu.web.core.ActionWriter;
import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.ActionResult;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ubuntu 2016/10/04.
 */
@Default
public class DefaultHandler extends AbstractHandler {

	private ApplicationPool pool;
	private ActionResolver actionResolver;
	private ActionExecutor actionExecutor;
	private ActionWriter actionWriter;

	public DefaultHandler() {
		this.pool = ApplicationPool.instance;
		this.actionResolver = new ActionResolver();
		this.actionExecutor = new ActionExecutor();
		this.actionWriter = new ActionWriter();
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// create action container based with request
		ActionContainer actionContainer = actionResolver.resolve(request, pool.getRequestActionPool());

		// execute action container
		ActionResult actionResult = actionExecutor.execute(actionContainer, pool.getInstancePool());

		// write response
		actionWriter.write(baseRequest, response, actionResult);
	}
}
