package com.mxpioframework.multitenant;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AutoConfigurationPackage
@ComponentScan
public class MultitenantConfiguration {
	
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager() {
		return new MultitenantJpaTransactionManager();
	}

}
