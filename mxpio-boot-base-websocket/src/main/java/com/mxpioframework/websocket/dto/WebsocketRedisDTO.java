package com.mxpioframework.websocket.dto;

import java.io.Serializable;
import java.util.Objects;

public class WebsocketRedisDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String endpoint;

    private String id;

    private String text;

    public WebsocketRedisDTO() {}

    public WebsocketRedisDTO(String endpoint, String id, String text) {
        this.endpoint = endpoint;
        this.id = id;
        this.text = text;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WebsocketRedisDTO that = (WebsocketRedisDTO) o;
        return Objects.equals(endpoint, that.endpoint) && Objects.equals(id, that.id) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpoint, id, text);
    }
}
