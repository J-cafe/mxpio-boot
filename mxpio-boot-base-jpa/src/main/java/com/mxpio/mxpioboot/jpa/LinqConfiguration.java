package com.mxpio.mxpioboot.jpa;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mxpio.mxpioboot.jpa.initiator.JpaUtilAble;
import com.mxpio.mxpioboot.jpa.initiator.JpaUtilInitiator;
import com.mxpio.mxpioboot.jpa.policy.CriteriaPolicy;
import com.mxpio.mxpioboot.jpa.policy.impl.QBCCriteriaPolicy;

/**
 * 模块配置类
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2017年11月16日
 */
@Configuration
@AutoConfigurationPackage
@ComponentScan
public class LinqConfiguration implements ApplicationContextAware {

	@Autowired
	List<JpaUtilInitiator> jpaUtilInitiators;
	
	@Autowired(required = false)
	List<JpaUtilAble> jpaUtilAbles;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		for (JpaUtilInitiator jpaUtilInitiator : jpaUtilInitiators) {
			jpaUtilInitiator.initialize(applicationContext);
		}
		if (jpaUtilAbles != null) {
			for (JpaUtilAble jpaUtilAble : jpaUtilAbles) {
				jpaUtilAble.afterPropertiesSet(applicationContext);
			}
		}
	}

}
