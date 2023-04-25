package com.mxpioframework.websocket.handler;

import com.mxpioframework.websocket.WebSocketConnection;
import com.mxpioframework.websocket.manager.MxpioWebSocketManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public abstract class AbstractWebSocketHandler implements MxpioWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractWebSocketHandler.class);

    @Autowired
    private MxpioWebSocketManager mxpioWebSocketManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(session==null||session.getPrincipal()==null){
            return;
        }
        String username = session.getPrincipal().getName();
        logger.info(":websocket建立连接，{}",username);
        if(StringUtils.isNotBlank(username)){
            WebSocketConnection connection = new WebSocketConnection();
            connection.setId(username);
            connection.setSession(session);
            mxpioWebSocketManager.put(getEndpointName(), username,connection);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("收到消息:{}",message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
        logger.error("websocket连接错误",e);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session.getPrincipal().getName();
        logger.info(":websocket断开连接，{}",username);
        if(StringUtils.isNotBlank(username)){
            mxpioWebSocketManager.remove(getEndpointName(),username);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void send(String id, String text) {
        mxpioWebSocketManager.send(getEndpointName(),id,text);
    }

    @Override
    public void broadcast(String text) {
        mxpioWebSocketManager.broadcast(getEndpointName(),text);
    }
}
