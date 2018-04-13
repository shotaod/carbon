package org.carbon.web.core;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.container.ComputedPath;
import org.carbon.web.container.PathVariables;
import org.carbon.web.context.request.RequestPool;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.exception.ActionNotFoundException;
import org.carbon.web.mapping.ActionDefinition;
import org.carbon.web.mapping.ActionMappingContext;

/**
 * @author Shota Oda 2016/10/07.
 */
@Component
public class ActionFinder {

    private class PathCheckResult {
        private boolean match;
        private ActionDefinition action;
        private PathVariables pathVariables;

        public PathCheckResult() {
            pathVariables = new PathVariables();
        }

        public void setMatch(boolean match) {
            this.match = match;
        }

        public void setAction(ActionDefinition action) {
            this.action = action;
        }

        public void addBind(String varName, String value) {
            this.pathVariables.addValue(varName, value);
        }

        public boolean isMatch() {
            return match;
        }

        public ActionDefinition getAction() {
            return action;
        }

        public PathVariables getPathVariables() {
            return pathVariables;
        }
    }

    @Assemble
    private RequestPool requestPool;
    @Assemble
    private ActionMappingContext actionMappingContext;

    /**
     * search target action and set path variables to requet pool
     *
     * @param request not null
     */
    public ActionDefinition find(HttpServletRequest request) {
        // classify by HttpMethod
        List<ActionDefinition> actionDefinitions = filterByHttpMethod(request);

        // apply Action by Url
        return findAction(request, actionDefinitions);
    }

    private List<ActionDefinition> filterByHttpMethod(HttpServletRequest request) {
        HttpMethod httpMethod = HttpMethod.codeOf(request.getMethod());
        return actionMappingContext.getByHttpMethod(httpMethod);
    }

    private ActionDefinition findAction(HttpServletRequest request, List<ActionDefinition> actionDefinitions) {
        if (actionDefinitions == null) {
            throw actionNotFoundException(request);
        }
        return actionDefinitions.stream()
                .map(action -> checkPath(request.getPathInfo(), action))
                .filter(PathCheckResult::isMatch)
                .findFirst()
                .map(result -> {
                    ActionDefinition action = result.getAction();
                    requestPool.setObject(result.getPathVariables());
                    return action;
                })
                .orElseThrow(() -> actionNotFoundException(request));
    }

    private PathCheckResult checkPath(String requestPath, ActionDefinition action) {
        PathCheckResult result = new PathCheckResult();
        String[] requestParts = requestPath.split("/");
        ComputedPath computed = action.getComputed();
        List<ComputedPath.Node> computedPaths = computed.getComputedPaths();

        if (requestParts.length != computedPaths.size()) {
            result.setMatch(false);
            return result;
        }

        for (int i = 0; i < requestParts.length; i++) {
            String requestPart = requestParts[i];
            ComputedPath.Node defined = computedPaths.get(i);

            if (defined.isVar()) {
                result.addBind(defined.getVarName(), requestPart);
            } else if (!defined.getPathName().equals(requestPart)) {
                result.setMatch(false);
                return result;
            }
        }

        result.setMatch(true);
        result.setAction(action);
        return result;
    }

    private ActionNotFoundException actionNotFoundException(HttpServletRequest request) {
        return new ActionNotFoundException(
                String.format(
                        "[request] %s is not found in application action",
                        request.getRequestURI()
                )
        );
    }
}
