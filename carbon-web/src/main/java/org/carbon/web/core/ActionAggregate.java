package org.carbon.web.core;

import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.container.ActionResult;
import org.carbon.web.mapping.ActionDefinition;

/**
 * @author Shota Oda 2016/10/16.
 */
@Component
public class ActionAggregate {

    @Assemble
    private ActionFinder actionFinder;
    @Assemble
    private ActionExecutor actionExecutor;

    public ActionResult execute(HttpServletRequest request) throws Throwable {
        // create action container by url
        // with resolving url variable
        ActionDefinition requestAssociatedAction = actionFinder.find(request);

        // execute action container
        return actionExecutor.execute(requestAssociatedAction);
    }
}
