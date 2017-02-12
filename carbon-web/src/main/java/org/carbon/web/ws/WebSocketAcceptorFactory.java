package org.carbon.web.ws;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shota Oda 2017/01/06.
 */
public class WebSocketAcceptorFactory {
    private static class SimpleAcceptor implements WebSocketFactory.Acceptor {
        private WebSocket socket;

        public SimpleAcceptor(WebSocket socket) {
            this.socket = socket;
        }

        @Override
        public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
            return socket;
        }

        @Override
        public boolean checkOrigin(HttpServletRequest request, String origin) {
            return true;
        }
    }

    public static WebSocketFactory.Acceptor factorize(WebSocket webSocket) {
        return new SimpleAcceptor(webSocket);
    }
}
