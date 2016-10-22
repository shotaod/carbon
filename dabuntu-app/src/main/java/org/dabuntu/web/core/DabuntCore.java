package org.dabuntu.web.core;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.dabuntu.web.container.ActionContainer;
import org.dabuntu.web.container.ActionResult;
import org.dabuntu.web.container.request.ParsedRequest;
import org.dabuntu.web.context.ApplicationPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author ubuntu 2016/10/16.
 */
@Component
public class DabuntCore {

	private Logger logger = LoggerFactory.getLogger(DabuntCore.class);

	private ApplicationPool pool = ApplicationPool.instance;
	@Inject
	private ActionFinder actionFinder;
	@Inject
	private RequestParser requestParser;
	@Inject
	private ActionArgumentResolver actionArgumentResolver;
	@Inject
	private ActionExecutor actionExecutor;
	@Inject
	private ActionFinisher actionFinisher;

	public void execute(HttpServletRequest request, HttpServletResponse response) {

		// create action container by url
		// with resolving url variable
		ActionContainer actionContainer = actionFinder.find(request, pool.getActionPool());

		// parse Request for argument Resolver
		ParsedRequest parsedRequest = requestParser.parse(request);

		// create new action container which method param is resolve from request header & request body
		ActionContainer argResolvedActionContainer = actionArgumentResolver.resolve(parsedRequest, pool.getSessionPool(), actionContainer);

		// execute action container
		ActionResult actionResult = actionExecutor.execute(argResolvedActionContainer, pool.getAppPool());

		// finish response
		actionFinisher.finish(response, actionResult);
	}
}
