package com.mxpioframework.autoconfigure.dingtalk;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.dingtalk.DingTalkConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(DingTalkConfiguration.class)
@Import(DingTalkConfiguration.class)
@Slf4j
public class DingTalkAutoConfiguration {
    public DingTalkAutoConfiguration() {
        log.info("[AutoConfiguration==>]:DingTalk Module Loading");

        CommonConstant.addModule(new ModuleVO("DingTalk", "钉钉模块"));
    }
}
