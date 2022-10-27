package com.mxpioframework.expression.func.provider.impl;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.googlecode.aviator.AviatorEvaluator;
import com.mxpioframework.expression.func.provider.FuncProvider;
import com.mxpioframework.expression.func.type.AbstractSpringAviatorFunction;

@Component
public class SpringFuncProvider implements ApplicationContextAware, FuncProvider {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<AbstractSpringAviatorFunction> providers=applicationContext.getBeansOfType(AbstractSpringAviatorFunction.class).values();
		System.out.println("===========SpringAviatorFunction loading start ==========");
		for(AbstractSpringAviatorFunction provider:providers){
			if(provider.disabled() || provider.getName()==null){
				System.out.println("[mxpio-expression]Function==>"+provider.getName()+"(disabled)");
				continue;
			}
			System.out.println("[mxpio-expression]Function==>"+provider.getName()+"(enabled)");
			AviatorEvaluator.addFunction(provider);
		}
		System.out.println("===========SpringAviatorFunction  loading end  ==========");
	}

}
