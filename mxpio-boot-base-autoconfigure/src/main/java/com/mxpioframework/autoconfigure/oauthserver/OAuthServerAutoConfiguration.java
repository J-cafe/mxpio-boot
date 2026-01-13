package com.mxpioframework.autoconfigure.oauthserver;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.oauth.server.OAuthServerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@ConditionalOnClass(OAuthServerConfiguration.class)
@Import(OAuthServerConfiguration.class)
@Slf4j
public class OAuthServerAutoConfiguration {
    public OAuthServerAutoConfiguration() {
        log.info("[AutoConfiguration==>]:OAuth Server Module Loading");

        CommonConstant.addModule(new ModuleVO("OAuthServer", "OAuth Sever模块"));
    }
}
