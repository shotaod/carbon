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
	private Authenticator authenticator;
	@Inject
	private ActionArgumentResolver actionArgumentResolver;
	@Inject
	private ActionExecutor actionExecutor;
	@Inject
	private ActionFinisher actionFinisher;

	public void execute(HttpServletRequest request, HttpServletResponse response) {

		boolean authenticate = authenticator.authenticate(pool.getAuthLogicPool(), pool.getSessionPool(), request, response);
		if (!authenticate) return;

		// create action container by url
		// with resolving url variable
		// with auth
		ActionContainer actionContainer = actionFinder.find(request, pool.getActionPool());

		// create new action container which method param is resolve from request header & request body
		ActionContainer argResolvedActionContainer = actionArgumentResolver.resolve(request, pool.getSessionPool(), actionContainer);

		// execute action container
		ActionResult actionResult = actionExecutor.execute(argResolvedActionContainer, pool.getAppPool());

		// finish response
		actionFinisher.finish(response, actionResult);
	}
}
