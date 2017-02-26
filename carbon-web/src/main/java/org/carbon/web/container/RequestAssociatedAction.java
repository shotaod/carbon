package org.carbon.web.container;

import org.carbon.web.core.ActionArgumentAggregator;
import org.carbon.web.mapping.ActionDefinition;

/**
 * @author Shota Oda 2016/10/05.
 */
public class RequestAssociatedAction {
    private ActionDefinition actionDefinition;
    private PathVariableValues pathVariableValues;

    public RequestAssociatedAction(ActionDefinition actionDefinition, PathVariableValues pathVariableValues) {
        this.actionDefinition = actionDefinition;
        this.pathVariableValues = pathVariableValues;
    }

    public Object execute(ActionArgumentAggregator aggregator) throws Exception {
        return actionDefinition.execute(aggregator.with(pathVariableValues));
    }

    public ActionDefinition getActionDefinition() {
        return actionDefinition;
    }
}
