package org.carbon.web.core;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.container.ComputedUrl;
import org.carbon.web.container.PathVariableValues;
import org.carbon.web.container.RequestAssociatedAction;
import org.carbon.web.mapping.ActionMappingContext;
import org.carbon.web.mapping.ActionDefinition;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.exception.ActionNotFoundException;

/**
 * @author Shota Oda 2016/10/07.
 */
@Component
public class ActionFinder {

    private class UrlMatchResult {
        private boolean match;
        private ActionDefinition action;
        private PathVariableValues pathVariableValues;

        public UrlMatchResult() {
            pathVariableValues = new PathVariableValues();
        }

        public void setMatch(boolean match) {
            this.match = match;
        }

        public void setAction(ActionDefinition action) {
            this.action = action;
        }

        public void addBind(String varName, String value) {
            this.pathVariableValues.addValue(varName, value);
        }

        public boolean isMatch() {
            return match;
        }

        public RequestAssociatedAction getActionContainer() {
            return new RequestAssociatedAction(
                action,
                this.pathVariableValues
            );
        }
    }

    @Inject
    private ActionMappingContext actionMappingContext;

    public RequestAssociatedAction find(HttpServletRequest request) {
        // classify by HttpMethod
        List<ActionDefinition> actionDefinitions = filterByHttpMethod(request);

        // apply Action by Url
        return findAction(request, actionDefinitions);
    }

    private List<ActionDefinition> filterByHttpMethod(HttpServletRequest request) {
        HttpMethod httpMethod = HttpMethod.codeOf(request.getMethod());
        return actionMappingContext.getByHttpMethod(httpMethod);
    }

    private RequestAssociatedAction findAction(HttpServletRequest request, List<ActionDefinition> actionDefinitions) {
        if (actionDefinitions == null) {
            throw actionNotFoundException(request);
        }
        return actionDefinitions.stream()
            .map(action -> matchUrl(request.getPathInfo(), action))
            .filter(UrlMatchResult::isMatch)
            .findFirst()
            .map(UrlMatchResult::getActionContainer)
            .orElseThrow(() -> actionNotFoundException(request));
    }

    private UrlMatchResult matchUrl(String requestPath, ActionDefinition action) {
        UrlMatchResult result = new UrlMatchResult();
        String[] requestParts = requestPath.split("/");
//        if (requestParts.length == 0) {
//            requestParts = new String[] {""};
//        }
        ComputedUrl computed = action.getComputed();
        List<ComputedUrl.Path> computedPaths = computed.getComputedPaths();

        if(requestParts.length != computedPaths.size()) {
            result.setMatch(false);
            return result;
        }

        for (int i = 0; i < requestParts.length; i++) {
            String requestPart = requestParts[i];
            ComputedUrl.Path defined = computedPaths.get(i);

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
