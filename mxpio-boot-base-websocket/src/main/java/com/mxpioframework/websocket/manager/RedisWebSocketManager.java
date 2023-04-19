package com.mxpioframework.websocket.manager;

import com.mxpioframework.websocket.WebSocketConnection;


//TODO:基于redis，支持集群部署
public class RedisWebSocketManager implements WebSocketManager{
    @Override
    public WebSocketConnection get(String id) {
        return null;
    }

    @Override
    public void put(String id, WebSocketConnection webSocket) {

    }

    @Override
    public void remove(String id) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void send(String id, String text) {

    }

    @Override
    public void broadcast(String text) {

    }
}
