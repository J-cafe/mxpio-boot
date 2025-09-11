package com.mxpioframework.websocket.listener;


import com.mxpioframework.websocket.Constants;
import com.mxpioframework.websocket.condition.WebsocketRedisModeCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@Conditional(WebsocketRedisModeCondition.class)
public class ListenerContainerConfig {

    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory, RedisMessageListener redisMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 添加监听器监听指定的频道
        container.addMessageListener(redisMessageListener, new ChannelTopic(Constants.WEBSOCKET_REDIS_PUB_CHANNEL));
        return container;
    }
}
