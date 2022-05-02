package com.mxpioframework.autoconfigure.expression;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mxpioframework.autoconfigure.common.CommonAutoConfiguration;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.vo.ModuleVO;
import com.mxpioframework.expression.ExpressionConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * 自动装载 工作流模块 mxpio-boot-base-expression
 * @author MxpIO
 *
 */
@Configuration
@ConditionalOnClass(ExpressionConfiguration.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
@Import(ExpressionConfiguration.class)
@Slf4j
public class ExpressionAutoConfiguration {
	
	public ExpressionAutoConfiguration() {
		log.info("[AutoConfiguration==>]:Expression Module Loading");
		CommonConstant.addModule(new ModuleVO("Expression","表达式模块"));
	}

}
