package com.mxpioframework.autoconfigure.wechat;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.wechat.WeChatConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author : wpp
 * @Datetime : 2023/10/7 16:22
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Configuration
@ConditionalOnClass(WeChatConfiguration.class)
@Import(WeChatConfiguration.class)
@Slf4j
public class WeChatAutoConfiguration {
    public WeChatAutoConfiguration() {
        log.info("[AutoConfiguration==>]:WeChat Module Loading");

        CommonConstant.addModule(new ModuleVO("WeChat", "微信模块"));
    }
}
