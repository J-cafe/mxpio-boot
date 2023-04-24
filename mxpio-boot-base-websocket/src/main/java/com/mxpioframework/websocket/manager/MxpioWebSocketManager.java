package com.mxpioframework.websocket.manager;

import com.mxpioframework.websocket.WebSocketConnection;

public interface MxpioWebSocketManager {
    WebSocketConnection get(String endpoint,String id);

    void put(String endpoint, String id, WebSocketConnection webSocket);

    void remove(String endpoint, String id);

    int size(String endpoint);

    void send(String endpoint, String id, String text);

    void broadcast(String endpoint, String text);

}
