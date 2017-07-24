package org.carbon.web.mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;
import org.carbon.component.annotation.Inject;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.Socket;
import org.carbon.web.core.PathVariableResolver;
import org.carbon.web.def.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shota Oda 2016/10/05.
 */
@Configuration
public class ActionMappingConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ActionMappingConfiguration.class);

    @Inject
    private PathVariableResolver pathVariableResolver;
    @Inject
    private ControllerActionFactory controllerActionFactory;
    @Inject
    private WebSocketActionFactory socketAdapterActionFactory;

    @Assemble({Controller.class, Socket.class})
    private List<Object> endpoints;

    @Component
    public ActionMappingContext map() {
        Map<HttpMethod, List<ActionDefinition>> collect = endpoints.stream()
                .flatMap(instance -> {
                    Class<?> clazz = instance.getClass();
                    // actions
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        return controllerActionFactory.factorize(instance);
                    } else if (clazz.isAnnotationPresent(Socket.class)) {
                        return Stream.of(socketAdapterActionFactory.factorize(instance));
                    }
                    return Stream.empty();
                })
                .collect(Collectors.groupingBy(
                    ActionDefinition::getHttpMethod,
                    Collectors.toList()
                ));



        if (logger.isInfoEnabled()) {
            loggingResult(collect);
        }



        return new ActionMappingContext(collect);
    }

    private void loggingResult(Map<HttpMethod, List<ActionDefinition>> data) {
        List<SimpleKeyValue<String, ?>> kvs = data.entrySet().stream().flatMap(e -> {
            String hMethod = e.getKey().getCode();
            return e.getValue().stream().map(definedAction -> {
                String url = definedAction.getComputed().toString();
                if (url.isEmpty()) url = "/";
                String info = definedAction.mappingResult();
                String separator = Stream.generate(() -> " ").limit(6 - hMethod.length()).collect(Collectors.joining("", "", ": "));
                return new SimpleKeyValue<>(hMethod + separator + url, info);
            });
        }).collect(Collectors.toList());
        String boxedTitleLines = BoxedTitleMessage.produceLeft(kvs);
        String s = ChapterAttr.getBuilder("Mapping Result").appendLine(boxedTitleLines).toString();
        logger.info(s);
    }
}
