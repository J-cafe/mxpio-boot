package com.mxpioframework.security.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class ApplicationContextProvider implements ApplicationContextAware {
	
	private static ApplicationContext applicationContextSpring;
	 
    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContextSpring = applicationContext;
    }
 
    /**
     * 通过class 获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContextSpring.getBean(clazz);
    }
    
    /**
     * 通过class 获取Bean
     */
    public static <T> Map<String,T> getBeansOfType(Class<T> clazz) {
        return applicationContextSpring.getBeansOfType(clazz);
    }

	public static ApplicationContext getApplicationContextSpring() {
		return applicationContextSpring;
	}

	public static void setApplicationContextSpring(ApplicationContext applicationContextSpring) {
		ApplicationContextProvider.applicationContextSpring = applicationContextSpring;
	}

	public static Environment getEnvironment() {
		return applicationContextSpring.getEnvironment();
	}
	
}
