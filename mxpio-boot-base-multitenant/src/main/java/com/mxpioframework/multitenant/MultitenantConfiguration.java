package com.mxpioframework.multitenant;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AutoConfigurationPackage
@ComponentScan
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class, DataSourceProperties.class})
public class MultitenantConfiguration {
	
	@Bean
	@Primary
	public PlatformTransactionManager transactionManager() {
		return new MultitenantJpaTransactionManager();
	}

}
