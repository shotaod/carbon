package org.carbon.web.core.mapping;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.web.annotation.Channeled;
import org.carbon.web.annotation.OnClose;
import org.carbon.web.annotation.OnOpen;
import org.carbon.web.annotation.OnReceive;
import org.carbon.web.annotation.Socket;
import org.carbon.web.container.ActionResult;
import org.carbon.web.container.ComputedUrl;
import org.carbon.web.core.ActionArgumentAggregator;
import org.carbon.web.core.InstanceSource;
import org.carbon.web.core.PathVariableResolver;
import org.carbon.web.def.HttpMethod;
import org.carbon.web.ws.ChannelStation;
import org.carbon.web.ws.ChannelWebSocketAdapter;
import org.carbon.web.ws.WebSocketAcceptorFactory;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Shota Oda 2017/01/04.
 */
@Component
public class WebSocketActionFactory {

    private static class AdapterAction extends ActionDefinition {
        private Class controller;
        private Function<ActionArgumentAggregator, WebSocket> socketImplDelegate;

        public AdapterAction(HttpMethod httpMethod, ComputedUrl computed, Class clazz) {
            super(httpMethod, computed);
            this.controller = clazz;
        }

        @Override
        public Object execute(ActionArgumentAggregator aggregator) throws Exception {
            HttpServletRequest request = aggregator.find(HttpServletRequest.class, InstanceSource.Request);
            HttpServletResponse response = aggregator.find(HttpServletResponse.class, InstanceSource.Request);
            WebSocket webSocket = socketImplDelegate.apply(aggregator);
            WebSocketFactory.Acceptor acceptor = WebSocketAcceptorFactory.factorize(webSocket);

            new WebSocketFactory(acceptor).acceptWebSocket(request, response);

            return ActionResult.NoOp();
        }

        @Override
        public String mappingResult() {
            String controllerName = controller.getName();
            return "ws://" + controllerName;
        }
    }

    @Inject
    private PathVariableResolver pathResolver;

    @SuppressWarnings("unchecked")
    public ActionDefinition factorize(Object instance) {
        Class<?> socketClass = instance.getClass();
        Socket annotation = socketClass.getDeclaredAnnotation(Socket.class);

        String url = annotation.url();

        Map<Class<? extends Annotation>, Method> methods = new HashMap<>();
        Stream<Parameter> params = Arrays.stream(socketClass.getDeclaredMethods())
                .flatMap(method -> {
                    if (method.isAnnotationPresent(OnOpen.class)) {
                        methods.put(OnOpen.class, method);
                        return paramStream(method);
                    } else if (method.isAnnotationPresent(OnClose.class)) {
                        methods.put(OnClose.class, method);
                        return paramStream(method);
                    } else if (method.isAnnotationPresent(OnReceive.class)) {
                        methods.put(OnReceive.class, method);
                        return paramStream(method);
                    } else if (method.isAnnotationPresent(Channeled.class)) {
                        methods.put(Channeled.class, method);
                        return paramStream(method);
                    } else return Stream.empty();
                });

        ComputedUrl computed = pathResolver.resolve(url, params);

        AdapterAction adapterAction = new AdapterAction(HttpMethod.GET, computed, socketClass);
        adapterAction.socketImplDelegate = aggregator -> {
            ChannelWebSocketAdapter adapter= new ChannelWebSocketAdapter(ChannelStation.instance);
            Method onOpen = methods.get(OnOpen.class);
            if (onOpen != null) {
                adapter.setOnOpen(aggregator.resolve(onOpen, instance));
            }
            Method onClose = methods.get(OnClose.class);
            if (onClose != null) {
                adapter.setOnClose(aggregator.resolve(onClose, instance));
            }
            Method onReceive = methods.get(OnReceive.class);
            if (onReceive != null) {
                adapter.setOnReceive(aggregator.resolve(onReceive, instance));
            }
            Method onConfig = methods.get(Channeled.class);
            if (onConfig != null) {
                adapter.setOnConfig(aggregator.resolve(onConfig, instance));
            }
            return adapter;
        };
        return adapterAction;
    }

    private Stream<Parameter> paramStream(Method method) {
        return Stream.of(method.getParameters());
    }
}
