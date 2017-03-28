package org.carbon.web.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.ActionResult;
import org.carbon.web.container.RequestAssociatedAction;
import org.carbon.web.mapping.ActionMappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/16.
 */
@Component
public class CarbonCore {

    @Inject
    private ActionFinder actionFinder;
    @Inject
    private ActionExecutor actionExecutor;
    @Inject
    private ActionFinisher actionFinisher;

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        // create action container by url
        // with resolving url variable
        // with auth
        RequestAssociatedAction requestAssociatedAction = actionFinder.find(request);

        // execute action container
        ActionResult actionResult = actionExecutor.execute(requestAssociatedAction);

        // finish response
        actionFinisher.finish(response, actionResult);
    }
}
