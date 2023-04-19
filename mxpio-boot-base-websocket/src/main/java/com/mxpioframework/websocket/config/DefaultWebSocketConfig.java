package com.mxpioframework.websocket.config;


import com.mxpioframework.websocket.handler.DefaultWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
public class DefaultWebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private DefaultWebSocketHandler defaultWebSocketHandler;

    private static final Logger logger = LoggerFactory.getLogger(DefaultWebSocketConfig.class);
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>DefaultWebSocketConfig");
        registry.addHandler(defaultWebSocketHandler,"/default").setAllowedOrigins("*");
    }
}
