package com.mxpioframework.websocket.handler;

import org.springframework.web.socket.WebSocketHandler;

public interface MxpioWebSocketHandler extends WebSocketHandler {
    void send(String id,String text);

    void send(String id,String text,String clientType);

    void broadcast(String text);

    void broadcast(String text,String clientType);

    String getEndpointName();
}
