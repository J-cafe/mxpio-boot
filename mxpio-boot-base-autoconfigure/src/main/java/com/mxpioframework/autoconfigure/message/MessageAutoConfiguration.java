package com.mxpioframework.autoconfigure.message;


import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.message.MessageConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(MessageConfiguration.class)
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@Import(MessageConfiguration.class)
@Slf4j
public class MessageAutoConfiguration {

    public MessageAutoConfiguration(){
        log.info("[AutoConfiguration==>]:Message Module Loading");
        CommonConstant.addModule(new ModuleVO("Message","消息模块"));
    }
}
