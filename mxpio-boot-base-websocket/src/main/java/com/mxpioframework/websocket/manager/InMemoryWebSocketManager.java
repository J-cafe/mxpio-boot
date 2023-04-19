package com.mxpioframework.websocket.manager;

import com.mxpioframework.websocket.WebSocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryWebSocketManager implements WebSocketManager{
    private final Map<String, WebSocketConnection> webSocketPool = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(InMemoryWebSocketManager.class);

    @Override
    public WebSocketConnection get(String id) {
        return webSocketPool.get(id);
    }

    @Override
    public void put(String id, WebSocketConnection webSocket) {
        if(!webSocketPool.containsKey(id)){
            webSocketPool.put(id,webSocket);
        }
    }

    @Override
    public void remove(String id) {
        webSocketPool.remove(id);
    }

    @Override
    public int size() {
        return webSocketPool.size();
    }

    @Override
    public void send(String id, String text) {
        WebSocketConnection connection = webSocketPool.get(id);
        if(connection==null){
            logger.error("send>>>>>>id:{}的连接不存在",id);
            throw new RuntimeException("id为"+id+"的连接不存在");
        }
        try {
            WebSocketMessage<String> message = new TextMessage(text);
            connection.getSession().sendMessage(message);
        } catch (IOException e) {
            logger.error("send>>>>>>向id:{}的连接发送信息出现异常",id,e);
            throw new RuntimeException("发送异常");
        }
    }

    @Override
    public void broadcast(String text) {
        WebSocketMessage<String> message = new TextMessage(text);
        for(Map.Entry<String, WebSocketConnection> entry:webSocketPool.entrySet()){
            try {
                entry.getValue().getSession().sendMessage(message);
            } catch (IOException e) {
                logger.error("broadcast>>>>>>向id:{}的连接发送信息出现异常",entry.getKey(),e);
            }
        }
    }

}
