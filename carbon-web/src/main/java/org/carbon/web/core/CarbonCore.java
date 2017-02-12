package org.carbon.web.core;

import org.carbon.web.context.ApplicationPool;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.RequestAssociatedAction;
import org.carbon.web.container.ActionResult;
import org.carbon.web.context.ActionDefinitionContainer;
import org.carbon.web.context.SecurityContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shota Oda 2016/10/16.
 */
@Component
public class CarbonCore {

    private Logger logger = LoggerFactory.getLogger(CarbonCore.class);

    private ApplicationPool pool = ApplicationPool.instance;
    @Inject
    private ActionFinder actionFinder;
    @Inject
    private Authenticator authenticator;
    @Inject
    private ActionExecutor actionExecutor;
    @Inject
    private ActionFinisher actionFinisher;

    private ActionDefinitionContainer actionPool;
    private SecurityContainer securityPool;

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        if (securityPool == null) {
            securityPool = pool.getAppPool().getByType(SecurityContainer.class);
        }
        boolean authenticate = authenticator.authenticate(securityPool, request, response);
        if (!authenticate) return;

        // create action container by url
        // with resolving url variable
        // with auth
        if (actionPool == null) {
            actionPool = pool.getAppPool().getByType(ActionDefinitionContainer.class);
        }
        RequestAssociatedAction requestAssociatedAction = actionFinder.find(request, actionPool);

        // execute action container
        ActionResult actionResult = actionExecutor.execute(requestAssociatedAction);

        // finish response
        actionFinisher.finish(response, actionResult);
    }
}
