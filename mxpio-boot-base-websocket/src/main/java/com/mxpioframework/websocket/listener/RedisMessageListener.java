package com.mxpioframework.websocket.listener;

import com.alibaba.fastjson.JSON;
import com.mxpioframework.websocket.Constants;
import com.mxpioframework.websocket.condition.WebsocketRedisModeCondition;
import com.mxpioframework.websocket.dto.WebsocketRedisDTO;
import com.mxpioframework.websocket.manager.MxpioWebSocketManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;


@Component
@Conditional(WebsocketRedisModeCondition.class)
public class RedisMessageListener implements MessageListener {

    @Autowired
    private MxpioWebSocketManager websocketManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        if(!StringUtils.equals(channel, Constants.WEBSOCKET_REDIS_PUB_CHANNEL)){
            return;
        }
        String body = new String(message.getBody());
        WebsocketRedisDTO dto = JSON.parseObject(body,WebsocketRedisDTO.class);

        if(StringUtils.isBlank(dto.getId())){            //boradcast
            if(StringUtils.isNotBlank(dto.getEndpoint())){
                websocketManager.broadcastToEndpoint(dto.getEndpoint(),dto.getText());
            }
            else{
                websocketManager.broadcastToEndpoint(dto.getText());
            }
        }
        else{                                            //send
            if(StringUtils.isNotBlank(dto.getEndpoint())){
                websocketManager.sendToEndpoint(dto.getEndpoint(),dto.getId(),dto.getText());
            }
            else{
                websocketManager.sendToEndpoint(dto.getId(),dto.getText());
            }
        }

    }
}
