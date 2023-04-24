package com.mxpioframework.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket连接
 */
public class WebSocketConnection {

    /**
     * 唯一标识符
     */
    private String id;

    /**
     * 绑定用户session
     */
    private WebSocketSession session;

    private Map<String,Object> attributes = new ConcurrentHashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key,Object value){
        this.attributes.put(key,value);
    }
}
