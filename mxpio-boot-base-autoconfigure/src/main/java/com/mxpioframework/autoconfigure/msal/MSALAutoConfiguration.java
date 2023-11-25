package com.mxpioframework.autoconfigure.msal;

import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.msal.MSALConfiguration;
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
@ConditionalOnClass(MSALConfiguration.class)
@Import(MSALConfiguration.class)
@Slf4j
public class MSALAutoConfiguration {
    public MSALAutoConfiguration() {
        log.info("[AutoConfiguration==>]:MSAL Module Loading");

        CommonConstant.addModule(new ModuleVO("MSAL", "MSAL模块"));
    }
}
