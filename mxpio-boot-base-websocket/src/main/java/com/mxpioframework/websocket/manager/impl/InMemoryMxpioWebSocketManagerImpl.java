package com.mxpioframework.websocket.manager.impl;

import com.mxpioframework.websocket.condition.WebsocketInMemoryModeCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(WebsocketInMemoryModeCondition.class)
public class InMemoryMxpioWebSocketManagerImpl extends AbstractMxpioWebSocketManager {


    @Override
    public void send(String endpoint, String id, String text) {
        super.sendToEndpoint(endpoint, id, text);
    }

    @Override
    public void send(String id, String text) {
        super.sendToEndpoint(id, text);
    }

    @Override
    public void broadcast(String endpoint, String text) {
        super.broadcastToEndpoint(endpoint, text);
    }

    @Override
    public void broadcast(String text) {
        super.broadcastToEndpoint(text);
    }

}
