package org.carbon.web.core.mapping;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.util.SimpleKeyValue;
import org.carbon.util.format.BoxedTitleMessage;
import org.carbon.util.format.ChapterAttr;
import org.carbon.web.annotation.Controller;
import org.carbon.web.annotation.Socket;
import org.carbon.web.core.PathVariableResolver;
import org.carbon.web.context.ActionDefinitionContainer;
import org.carbon.web.def.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2016/10/05.
 */
@Component
public class ActionMapper {

    private static Logger logger = LoggerFactory.getLogger(ActionMapper.class);

    @Inject
    private PathVariableResolver pathVariableResolver;
    @Inject
    private ControllerActionFactory controllerActionFactory;
    @Inject
    private WebSocketActionFactory socketAdapterActionFactory;

    public ActionDefinitionContainer map(List<Object> instances) {
        Map<HttpMethod, List<ActionDefinition>> collect = instances.stream()
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

        loggingResult(collect);

        return new ActionDefinitionContainer(collect);
    }

    private void loggingResult(Map<HttpMethod, List<ActionDefinition>> data) {
        List<SimpleKeyValue> kvs = data.entrySet().stream().flatMap(e -> {
            String hMethod = e.getKey().getCode();
            return e.getValue().stream().map(definedAction -> {
                String url = definedAction.getComputed().toString();
                String info = definedAction.mappingResult();
                String separator = Stream.generate(() -> " ").limit(5 - hMethod.length()).collect(Collectors.joining("", "", ": "));
                return new SimpleKeyValue(hMethod + separator + url, info);
            });
        }).collect(Collectors.toList());
        String boxedTitleLines = BoxedTitleMessage.produceLeft(kvs);
        String s = ChapterAttr.getBuilder("Mapping Result").appendLine(boxedTitleLines).toString();
        logger.info(s);
    }
}
