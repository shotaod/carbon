package org.carbon.web.context;

import org.carbon.web.def.HttpMethod;
import org.carbon.web.core.mapping.ActionDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author Shota Oda 2016/10/07.
 */
public class ActionDefinitionContainer {
    Map<HttpMethod, List<ActionDefinition>> container;

    public ActionDefinitionContainer(Map<HttpMethod, List<ActionDefinition>> container) {
        this.container = container;
    }

    public Map<HttpMethod, List<ActionDefinition>> getContainer() {
        return container;
    }
}
