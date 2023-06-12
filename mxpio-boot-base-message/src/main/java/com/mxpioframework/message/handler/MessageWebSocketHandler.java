package com.mxpioframework.message.handler;


import com.mxpioframework.websocket.handler.AbstractWebSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class MessageWebSocketHandler extends AbstractWebSocketHandler {
    @Override
    public String getEndpointName() {
        return "ws/message";
    }
}
