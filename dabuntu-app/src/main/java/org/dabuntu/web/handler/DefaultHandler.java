package org.dabuntu.web.handler;

import org.dabuntu.util.format.TagAttr;
import org.dabuntu.web.container.request.ParsedRequest;
import org.dabuntu.web.core.ActionArgumentResolver;
import org.dabuntu.web.core.ActionExecutor;
import org.dabuntu.web.annotation.Default;
import org.dabuntu.web.context.ApplicationPool;
import org.dabuntu.web.core.ActionFinder;
import org.dabuntu.web.core.ActionWriter;
import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.core.RequestParser;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author ubuntu 2016/10/04.
 */
@Default
public class DefaultHandler extends HandlerWrapper {

	private Logger logger = LoggerFactory.getLogger(DefaultHandler.class);

	private ApplicationPool pool;
	private ActionFinder actionFinder;
	private RequestParser requestParser;
	private ActionArgumentResolver actionArgumentResolver;
	private ActionExecutor actionExecutor;
	private ActionWriter actionWriter;

	public DefaultHandler() {
		this.pool = ApplicationPool.instance;
		this.actionFinder = new ActionFinder();
		this.requestParser = new RequestParser();
		this.actionArgumentResolver = new ActionArgumentResolver();
		this.actionExecutor = new ActionExecutor();
		this.actionWriter = new ActionWriter();
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// TODO Request Filtering
		final boolean createIfNecessary = true;
		HttpSession session = request.getSession(createIfNecessary);

		Enumeration<String> sessionAttributeNames = session.getAttributeNames();
		logger.debug(TagAttr.get("Sessions"));
		while (sessionAttributeNames.hasMoreElements()) {
			logger.debug(sessionAttributeNames.nextElement());
		}

		// create action container by url
		// with resolving url variable
		ActionContainer actionContainer = actionFinder.find(request, pool.getActionPool());

		// parse Request for argument Resolver
		ParsedRequest parsedRequest = requestParser.parse(request);

		// create new action container which method param is resolve from request header & request body
		ActionContainer argResolvedActionContainer = actionArgumentResolver.resolve(parsedRequest, pool.getSessionPool(), actionContainer);

		// execute action container
		ActionResult actionResult = actionExecutor.execute(argResolvedActionContainer, pool.getInstancePool());

		// write response
		actionWriter.write(baseRequest, response, actionResult);

		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);

		super.handle(target, baseRequest, request, response);
		// TODO Response Filtering
	}
}
