package com.mxpioframework.websocket.handler;

import com.mxpioframework.security.util.SecurityUtils;
import com.mxpioframework.websocket.WebSocketConnection;
import com.mxpioframework.websocket.manager.WebSocketManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class AbstractWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractWebSocketHandler.class);

    @Autowired
    private WebSocketManager webSocketManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(session==null||session.getPrincipal()==null){
            return;
        }
        String username = session.getPrincipal().getName();
        if(StringUtils.isNotBlank(username)){
            WebSocketConnection connection = new WebSocketConnection();
            connection.setId(username);
            connection.setSession(session);
            webSocketManager.put(username,connection);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        webSocketManager.broadcast(message.getPayload().toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
        logger.error("websocket连接错误",e);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session.getPrincipal().getName();
        if(StringUtils.isNotBlank(username)){
            webSocketManager.remove(username);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
