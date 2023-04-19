package com.mxpioframework.websocket.manager;

import com.mxpioframework.websocket.WebSocketConnection;


//TODO:基于redis，支持集群部署
public class RedisMxpioWebSocketManager implements MxpioWebSocketManager {

    @Override
    public WebSocketConnection get(String endpoint, String id) {
        return null;
    }

    @Override
    public void put(String endpoint, String id, WebSocketConnection webSocket) {

    }

    @Override
    public void remove(String endpoint, String id) {

    }

    @Override
    public int size(String endpoint) {
        return 0;
    }

    @Override
    public void send(String endpoint, String id, String text) {

    }

    @Override
    public void broadcast(String endpoint, String text) {

    }
}
