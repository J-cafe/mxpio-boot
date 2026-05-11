package com.mxpioframework.websocket.condition;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

public class WebsocketRedisModeCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 检查redisTemplate类是否存在
        boolean isRedisTemplateExists = ClassUtils.isPresent("org.springframework.data.redis.core.RedisTemplate",
                context.getClassLoader());
        //如果没有引入redis，直接返回false不开启配置
        if(!isRedisTemplateExists){
            return false;
        }
        // 获取环境信息
        Environment env = context.getEnvironment();
        String clusterModeEnabled = env.getProperty("mxpio.websocket.cluster.enabled");
        return StringUtils.equalsIgnoreCase(clusterModeEnabled,"true");
    }
}
