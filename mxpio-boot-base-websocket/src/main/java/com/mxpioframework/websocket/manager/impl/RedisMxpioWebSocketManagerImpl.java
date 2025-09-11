package com.mxpioframework.websocket.manager.impl;


import com.alibaba.fastjson.JSON;
import com.mxpioframework.websocket.Constants;
import com.mxpioframework.websocket.condition.WebsocketRedisModeCondition;
import com.mxpioframework.websocket.dto.WebsocketRedisDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@Conditional(WebsocketRedisModeCondition.class)
public class RedisMxpioWebSocketManagerImpl extends AbstractMxpioWebSocketManager{


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void send(String endpoint, String id, String text) {
        WebsocketRedisDTO pojo = new WebsocketRedisDTO(endpoint,id,text);
        String json = JSON.toJSONString(pojo);
        redisTemplate.convertAndSend(Constants.WEBSOCKET_REDIS_PUB_CHANNEL,json);
    }

    @Override
    public void send(String id, String text) {
        this.send(null,id,text);
    }

    @Override
    public void broadcast(String endpoint, String text) {
        this.send(endpoint,null,text);
    }

    @Override
    public void broadcast(String text) {
        this.send(null,null,text);
    }

}
