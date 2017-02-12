package org.carbon.web.ws;

import org.eclipse.jetty.websocket.WebSocket;

/**
 * @author Shota Oda 2017/01/07.
 */
public interface ChanneledSocket extends WebSocket.OnTextMessage, WebSocket.OnBinaryMessage, Receiver{}