package com.mxpioframework.websocket.manager;

import com.mxpioframework.websocket.WebSocketConnection;

import java.util.Map;

public interface WebSocketManager {
    WebSocketConnection get(String id);

    void put(String id, WebSocketConnection webSocket);

    void remove(String id);

    int size();

    void send(String id, String text);

    void broadcast(String text);

}
