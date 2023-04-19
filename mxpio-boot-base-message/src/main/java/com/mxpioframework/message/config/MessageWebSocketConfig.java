package com.mxpioframework.message.config;


import com.mxpioframework.message.handler.MessageWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
public class MessageWebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MessageWebSocketHandler messageWebSocketHandler;

    private static final Logger logger = LoggerFactory.getLogger(MessageWebSocketConfig.class);
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>MessageWebSocketConfig");
        registry.addHandler(messageWebSocketHandler,"/message").setAllowedOrigins("*");
    }
}
