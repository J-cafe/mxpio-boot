package com.mxpioframework.websocket.manager.impl;

import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.websocket.WebSocketConnection;
import com.mxpioframework.websocket.manager.MxpioWebSocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMxpioWebSocketManager implements MxpioWebSocketManager {
    private final Map<String, Map<String, WebSocketConnection>> webSocketPool = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AbstractMxpioWebSocketManager.class);

    @Override
    public WebSocketConnection get(String endpoint, String id) {
        Map<String, WebSocketConnection> endpointWebSocket = webSocketPool.get(endpoint);
        if(endpointWebSocket==null){
            return null;
        }
        return endpointWebSocket.get(id);
    }

    @Override
    public void put(String endpoint,String id, WebSocketConnection webSocket) {
        if(!webSocketPool.containsKey(endpoint)){
            webSocketPool.put(endpoint,new ConcurrentHashMap<>());
        }
        webSocketPool.get(endpoint).put(id,webSocket);
    }

    @Override
    public void remove(String endpoint,String id) {
        webSocketPool.get(endpoint).remove(id);
    }

    @Override
    public int size(String endpoint) {
        return webSocketPool.get(endpoint).size();
    }

    @Override
    public void sendToEndpoint(String endpoint, String id, String text) {
        Map<String, WebSocketConnection> endpointWebSocket = getWebSocketPool().get(endpoint);
        if(endpointWebSocket==null){
            throw new MBootException("endpoint为"+endpoint+"的连接不存在");
        }
        WebSocketConnection connection = endpointWebSocket.get(id);
        if(connection==null){
            throw new MBootException("endpoint为"+endpoint+" id为"+id+"的连接不存在");
        }
        try {
            WebSocketMessage<String> message = new TextMessage(text);
            connection.getSession().sendMessage(message);
        } catch (IOException e) {
            throw new MBootException("发送异常");
        }
    }

    @Override
    public void sendToEndpoint(String id, String text) {
        for(Map<String, WebSocketConnection> endpointWebSocket:getWebSocketPool().values()){
            WebSocketConnection connection = endpointWebSocket.get(id);
            if(connection==null){
                logger.info("无连接");
                continue;
            }
            try {
                WebSocketMessage<String> message = new TextMessage(text);
                connection.getSession().sendMessage(message);
            } catch (IOException e) {
                logger.info("发送异常");
            }
        }
    }

    @Override
    public void broadcastToEndpoint(String endpoint, String text) {
        Map<String, WebSocketConnection> endpointWebSocket = getWebSocketPool().get(endpoint);
        if(endpointWebSocket==null){
            logger.error("broadcast>>>>>>endpoint:{}的连接不存在",endpoint);
            throw new MBootException("endpoint为"+endpoint+"的连接不存在");
        }
        WebSocketMessage<String> message = new TextMessage(text);
        for(Map.Entry<String, WebSocketConnection> entry:endpointWebSocket.entrySet()){
            try {
                entry.getValue().getSession().sendMessage(message);
            } catch (IOException e) {
                logger.error("broadcast>>>>>>向endpoint:{},id:{}的连接发送信息出现异常",endpoint,entry.getKey(),e);
            }
        }
    }

    @Override
    public void broadcastToEndpoint(String text) {
        for(Map<String, WebSocketConnection> endpointWebSocket:getWebSocketPool().values()){
            WebSocketMessage<String> message = new TextMessage(text);
            for(Map.Entry<String, WebSocketConnection> entry:endpointWebSocket.entrySet()){
                try {
                    entry.getValue().getSession().sendMessage(message);
                } catch (IOException e) {
                    logger.info("broadcast>>>>>>id:{}的连接发送信息出现异常",entry.getKey(),e);
                }
            }
        }

    }

    public Map<String, Map<String, WebSocketConnection>> getWebSocketPool() {
        return webSocketPool;
    }


}
