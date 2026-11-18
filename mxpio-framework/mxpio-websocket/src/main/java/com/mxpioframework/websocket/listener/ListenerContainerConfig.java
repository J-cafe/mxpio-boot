package com.mxpioframework.websocket.listener;


import com.mxpioframework.cache.CacheProperties;
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
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory, RedisMessageListener redisMessageListener, CacheProperties cacheProperties) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 添加监听器监听指定的频道（频道名带应用前缀，避免多应用共用Redis时互相串消息）
        container.addMessageListener(redisMessageListener, new ChannelTopic(cacheProperties.resolveKeyPrefix() + Constants.WEBSOCKET_REDIS_PUB_CHANNEL));
        return container;
    }
}
