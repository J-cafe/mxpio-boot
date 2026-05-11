package com.mxpioframework.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// endPoint 注册协议节点,并映射指定的URl
		// 注册一个Stomp 协议的endpoint,并指定 SockJS协议
		registry.addEndpoint("/endpointWisely").withSockJS();

		// 注册一个名字为"endpointChat" 的endpoint,并指定 SockJS协议。 点对点
		registry.addEndpoint("/endpointChat").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 配置消息代理(message broker)
		// 广播式应配置一个/topic 消息代理
		registry.enableSimpleBroker("/topic");

		// 点对点式增加一个/queue 消息代理
		registry.enableSimpleBroker("/queue", "/topic");

	}
}