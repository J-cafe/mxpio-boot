package com.mxpioframework.autoconfigure.email;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.email.EmailConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(EmailConfiguration.class)
@Import(EmailConfiguration.class)
@Slf4j
public class EmailAutoConfiguration {
    public EmailAutoConfiguration() {
        log.info("[AutoConfiguration==>]:Email Module Loading");

        CommonConstant.addModule(new ModuleVO("Email", "邮件消息模块"));
    }
}
