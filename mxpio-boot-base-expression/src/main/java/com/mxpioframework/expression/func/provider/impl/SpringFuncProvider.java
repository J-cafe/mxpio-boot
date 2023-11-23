package com.mxpioframework.expression.func.provider.impl;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.googlecode.aviator.AviatorEvaluator;
import com.mxpioframework.expression.func.provider.FuncProvider;
import com.mxpioframework.expression.func.type.AbstractSpringAviatorFunction;

@Slf4j
@Component
public class SpringFuncProvider implements ApplicationContextAware, FuncProvider {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<AbstractSpringAviatorFunction> functions=applicationContext.getBeansOfType(AbstractSpringAviatorFunction.class).values();
		log.info("===========SpringAviatorFunction loading start ==========");
		for(AbstractSpringAviatorFunction function:functions){
			if(function.disabled() || function.getName()==null){
				log.info("[mxpio-expression]Function==>"+function.getName()+"(disabled)");
				continue;
			}
			log.info("[mxpio-expression]Function==>"+function.getName()+"(enabled)");
			AviatorEvaluator.addFunction(function);
		}
		log.info("===========SpringAviatorFunction  loading end  ==========");

	}

}
