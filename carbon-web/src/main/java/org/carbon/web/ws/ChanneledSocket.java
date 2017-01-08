package org.carbon.web.ws;

import org.eclipse.jetty.websocket.WebSocket;

/**
 * @author ubuntu 2017/01/07.
 */
public interface ChanneledSocket extends WebSocket.OnTextMessage, WebSocket.OnBinaryMessage, Receiver{}