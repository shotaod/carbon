package org.carbon.web.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.carbon.component.annotation.Assemble;
import org.carbon.component.annotation.Component;
import org.carbon.web.annotation.ConfigureChannel;
import org.carbon.web.annotation.OnClose;
import org.carbon.web.annotation.OnOpen;
import org.carbon.web.annotation.OnReceive;
import org.carbon.web.annotation.Socket;
import org.carbon.web.container.ActionResult;
import org.carbon.web.container.ComputedPath;
import org.carbon.web.core.PathDefinitionResolver;
import org.carbon.web.core.args.ActionArgumentAggregatorFactory;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.ws.ChannelWebSocketAction;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.DecoratedObjectFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * @author Shota Oda 2017/01/04.
 */
@Component
public class WebSocketActionFactory {

    private interface WebSocketAcceptor {
        void accept(ActionArgumentAggregatorFactory.ActionArgumentAggregator aggregator) throws Exception;
    }

    private class AdapterAction extends ActionDefinition {
        private Class controller;
        private WebSocketAcceptor webSocketAcceptor;

        public AdapterAction(HttpMethod httpMethod, ComputedPath computed, WebSocketAcceptor webSocketAcceptor, Class clazz) {
            super(httpMethod, computed);
            this.controller = clazz;
            this.webSocketAcceptor = webSocketAcceptor;
        }

        @Override
        public Object execute(ActionArgumentAggregatorFactory.ActionArgumentAggregator aggregator) throws Exception {
            webSocketAcceptor.accept(aggregator);
            return ActionResult.Empty();
        }

        @Override
        public String mappingResult() {
            String controllerName = controller.getName();
            return "(ws://  )" + controllerName;
        }
    }

    @Assemble
    private PathDefinitionResolver pathResolver;
    @Assemble
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    public ActionDefinition factorize(Object instance) {
        Class<?> socketClass = instance.getClass();
        Socket annotation = socketClass.getDeclaredAnnotation(Socket.class);

        Map<Class<? extends Annotation>, Method> methods = new HashMap<>();
        Stream<Parameter> params = Arrays.stream(socketClass.getDeclaredMethods())
                .flatMap(method -> {
                    if (method.isAnnotationPresent(OnOpen.class)) {
                        methods.put(OnOpen.class, method);
                    } else if (method.isAnnotationPresent(OnClose.class)) {
                        methods.put(OnClose.class, method);
                    } else if (method.isAnnotationPresent(OnReceive.class)) {
                        methods.put(OnReceive.class, method);
                    } else if (method.isAnnotationPresent(ConfigureChannel.class)) {
                        methods.put(ConfigureChannel.class, method);
                    } else return Stream.empty();
                    return Stream.of(method.getParameters());
                });

        String path = annotation.path();
        ComputedPath computed = pathResolver.resolve(path, params);

        Method onOpen = methods.get(OnOpen.class);
        Method onClose = methods.get(OnClose.class);
        Method onReceive = methods.get(OnReceive.class);
        Method configurer = methods.get(ConfigureChannel.class);

        if (onReceive == null) {
            throw new IllegalStateException("@OnReceive is not defined");
        }

        WebSocketAcceptor acceptor = aggregator -> {
            ChannelWebSocketAction adapter = new ChannelWebSocketAction(
                    objectMapper,
                    aggregator.resolve(onReceive, instance),
                    Optional.ofNullable(onOpen).map(m -> aggregator.resolve(m, instance)).orElse(null),
                    Optional.ofNullable(onClose).map(m -> aggregator.resolve(m, instance)).orElse(null),
                    Optional.ofNullable(configurer).map(m -> aggregator.resolve(m, instance)).orElse(null)
            );
            WebSocketCreator creator = (req, resp) -> {
                for (String protocol : req.getSubProtocols()) {
                    if (Arrays.stream(annotation.protocols()).anyMatch(supported -> supported.equalsIgnoreCase(protocol))) {
                        resp.setAcceptedSubProtocol(protocol);
                        return adapter;
                    }
                }
                return null;
            };
            ContextHandler.Context currentContext = WebAppContext.getCurrentContext();
            currentContext.setAttribute(DecoratedObjectFactory.ATTR, new DecoratedObjectFactory());
            WebSocketServerFactory factory = new WebSocketServerFactory(currentContext, WebSocketPolicy.newServerPolicy());
            HttpServletRequest request = aggregator.getRequest();
            HttpServletResponse response = aggregator.getResponse();
            factory.start();
            factory.acceptWebSocket(creator, request, response);
        };
        return new AdapterAction(HttpMethod.GET, computed, acceptor, socketClass);
    }

}
