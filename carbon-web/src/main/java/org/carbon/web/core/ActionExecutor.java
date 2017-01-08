package org.carbon.web.core;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.ActionResult;
import org.carbon.web.container.RequestAssociatedAction;
import org.carbon.web.exception.ActionInvokeException;

/**
 * @author Shota Oda 2016/10/07.
 */
@Component
public class ActionExecutor {

    @Inject
    private ActionArgumentAggregator aggregator;

	public ActionResult execute(RequestAssociatedAction action) {
		// execute Action
        try {
            Object result = executeAction(action);
            if (result instanceof ActionResult) {
                return (ActionResult) result;
            }
            return ActionResult.Result(result);
        } catch (Exception e) {
            Exception exception = actionInvokeException(action.getActionDefinition().mappingResult(), e);
            return ActionResult.OnException(exception);
        }
    }

	// ===================================================================================
	//                                                                      Execute Method
	//                                                                      ==============
	private Object executeAction(RequestAssociatedAction action) throws Exception{
        return action.execute(aggregator);
	}

	private ActionInvokeException actionInvokeException (String mapping, Exception e) {
		String message = String.format("failed to Invoke Action at [%s]", mapping);
		return new ActionInvokeException(message, e);
	}
}
