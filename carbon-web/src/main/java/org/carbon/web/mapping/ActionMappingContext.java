package org.carbon.web.mapping;

import java.util.List;
import java.util.Map;

import org.carbon.web.mapping.ActionDefinition;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2016/10/07.
 */
public class ActionMappingContext {
    private Map<HttpMethod, List<ActionDefinition>> actionMap;

    public ActionMappingContext(Map<HttpMethod, List<ActionDefinition>> actionMap) {
        this.actionMap = actionMap;
    }

    public List<ActionDefinition> getByHttpMethod(HttpMethod method) {
        return actionMap.get(method);
    }
}
