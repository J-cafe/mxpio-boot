package com.mxpioframework.websocket.handler;

import com.mxpioframework.websocket.WebSocketConnection;
import com.mxpioframework.websocket.manager.MxpioWebSocketManager;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractWebSocketHandler implements MxpioWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(AbstractWebSocketHandler.class);

    @Autowired
    private MxpioWebSocketManager mxpioWebSocketManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(session==null||session.getPrincipal()==null){
            logger.info(":websocket建立连接，未获得用户信息");
            return;
        }

        String username = session.getPrincipal().getName();
        if(StringUtils.isNotBlank(username)){
            WebSocketConnection connection = new WebSocketConnection();
            connection.setId(username);
            connection.setSession(session);
            String clientType = getClientType(session);
            mxpioWebSocketManager.put(getEndpointName()+"_"+clientType, username,connection);
            logger.info(":websocket建立连接，clientType:{},username:{}",clientType,username);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("clientType:{}收到消息:{}",getClientType(session),message.getPayload());
        session.sendMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
        logger.error("websocket连接错误",e);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session.getPrincipal().getName();
        if(StringUtils.isNotBlank(username)){
            String clientType = getClientType(session);
            mxpioWebSocketManager.remove(getEndpointName()+"_"+clientType,username);
            logger.info(":websocket断开连接，clientType:{},username:{}",getClientType(session),username);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void send(String id, String text) {
        mxpioWebSocketManager.send(id,text);
    }
    @Override
    public void send(String id, String text,String clientType) {
        mxpioWebSocketManager.send(getEndpointName()+"_"+clientType,id,text);
    }

    @Override
    public void broadcast(String text) {
        mxpioWebSocketManager.broadcast(text);
    }

    @Override
    public void broadcast(String text,String clientType) {
        mxpioWebSocketManager.broadcast(getEndpointName()+"_"+clientType,text);
    }

    private Map<String, String> parseQuery(String query) {
        if (StringUtils.isBlank(query)){
            return Collections.emptyMap();
        }

        return Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr.length > 1 ? arr[1] : ""
                ));
    }

    private String getClientType(WebSocketSession session){
        String query = session.getUri().getQuery();
        Map<String, String> paramMap = parseQuery(query);
        String clientType = "DEFAULT";
        if(MapUtils.isNotEmpty(paramMap)&&paramMap.containsKey("type")&&StringUtils.isNotBlank(paramMap.get("type"))){
            clientType = paramMap.get("type");
        }
        return clientType;
    }

}
