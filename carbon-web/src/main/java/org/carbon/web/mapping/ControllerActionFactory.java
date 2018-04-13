package org.carbon.web.mapping;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Assemble;
import org.carbon.web.annotation.Action;
import org.carbon.web.annotation.Controller;
import org.carbon.web.container.ComputedPath;
import org.carbon.web.core.args.ActionArgumentAggregatorFactory;
import org.carbon.web.core.PathDefinitionResolver;
import org.carbon.web.def.HttpMethod;

/**
 * @author Shota Oda 2017/01/04.
 */
@Component
public class ControllerActionFactory {

    private static class ControllerAction extends ActionDefinition {

        private Object controller;
        private Method action;

        public ControllerAction(HttpMethod httpMethod, ComputedPath computed, Object controller, Method action) {
            super(httpMethod, computed);
            this.controller = controller;
            this.action = action;
        }

        @Override
        public Object execute(ActionArgumentAggregatorFactory.ActionArgumentAggregator aggregator) {
            return aggregator.resolve(action, controller).execute();
        }

        @Override
        public String mappingResult() {
            String controller = this.controller.getClass().getName();
            String method = this.action.getName();
            return "(http://)" + controller + "#" + method;
        }
    }

    @Assemble
    private PathDefinitionResolver pathResolver;

    public Stream<ActionDefinition> factorize(Object controller) {
        Class<?> controllerClass = controller.getClass();
        Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
        String controllerRootPath = controllerAnnotation.value();
        return Arrays.stream(controllerClass.getMethods())
            // filtering Action
            .filter(method -> method.isAnnotationPresent(Action.class))
            // convert Container
            .map(action -> {
                Action annotation = action.getDeclaredAnnotation(Action.class);
                HttpMethod method = annotation.method();
                Path path = Paths.get(controllerRootPath, annotation.path());
                ComputedPath computed = pathResolver.resolve(path, action);
                return new ControllerAction(method, computed, controller, action);
            });
    }
}
