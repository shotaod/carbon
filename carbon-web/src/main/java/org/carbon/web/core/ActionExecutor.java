package org.carbon.web.core;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.container.ActionResult;
import org.carbon.web.core.args.ActionArgumentAggregatorFactory;
import org.carbon.web.mapping.ActionDefinition;

/**
 * @author Shota Oda 2016/10/07.
 */
@Component
public class ActionExecutor {

    @Assemble
    private ActionArgumentAggregatorFactory aggregatorFactory;

    public ActionResult execute(ActionDefinition action) throws Throwable {
        // execute Action
        Object result = executeAction(action);
        if (result instanceof ActionResult) {
            return (ActionResult) result;
        }
        return ActionResult.Result(result);
    }

    // ===================================================================================
    //                                                                      Execute Method
    //                                                                      ==============
    private Object executeAction(ActionDefinition action) throws Throwable {
        return action.execute(aggregatorFactory.newAggregator());
    }
}
