package com.mxpioframework.autoconfigure.websocket;


import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.websocket.WebSocketConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(WebSocketConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(WebSocketConfiguration.class)
@Slf4j
public class WebSocketAutoConfiguration {

    public WebSocketAutoConfiguration(){
        log.info("[AutoConfiguration==>]:WebSocket Module Loading");
        CommonConstant.addModule(new ModuleVO("WebSocket","WebSocket模块"));
    }
}
