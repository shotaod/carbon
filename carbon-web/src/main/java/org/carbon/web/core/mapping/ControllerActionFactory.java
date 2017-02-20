package org.carbon.web.core.mapping;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.annotation.Action;
import org.carbon.web.container.ComputedUrl;
import org.carbon.web.core.ActionArgumentAggregator;
import org.carbon.web.core.PathVariableResolver;
import org.carbon.web.def.HttpMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2017/01/04.
 */
@Component
public class ControllerActionFactory {

    private static class ControllerAction extends ActionDefinition {

        private Object controller;
        private Method action;

        public ControllerAction(HttpMethod httpMethod, ComputedUrl computed, Object controller, Method action) {
            super(httpMethod, computed);
            this.controller = controller;
            this.action = action;
        }

        @Override
        public Object execute(ActionArgumentAggregator aggregator) throws Exception {
            return aggregator.resolve(action, controller).execute();
        }

        @Override
        public String mappingResult() {
            String controller = this.controller.getClass().getName();
            String method = this.action.getName();
            return "(http://)" + controller + "#" + method;
        }
    }

    @Inject
    private PathVariableResolver pathResolver;

    public Stream<ActionDefinition> factorize(Object controller) {
        Class<?> controllerClass = controller.getClass();
        return Arrays.stream(controllerClass.getMethods())
            // filtering Action
            .filter(method -> method.isAnnotationPresent(Action.class))
            // convert Container
            .map(action -> {
                Action annotation = action.getDeclaredAnnotation(Action.class);
                HttpMethod method = annotation.method();
                ComputedUrl computed = pathResolver.resolve(annotation.url(), action);
                return new ControllerAction(method, computed, controller, action);
            });
    }
}
