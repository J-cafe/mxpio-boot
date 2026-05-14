package com.mxpioframework.autoconfigure.oauth;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.oauth.OAuthConfiguration;
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
@ConditionalOnClass(OAuthConfiguration.class)
@Import(OAuthConfiguration.class)
@Slf4j
public class OAuthAutoConfiguration {
    public OAuthAutoConfiguration() {
        log.info("[AutoConfiguration==>]:OAuth Module Loading");

        CommonConstant.addModule(new ModuleVO("OAuth", "OAuth模块"));
    }
}
