package org.carbon.web.core;

import org.carbon.web.context.ApplicationPool;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.ActionContainer;
import org.carbon.web.container.ActionResult;
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
