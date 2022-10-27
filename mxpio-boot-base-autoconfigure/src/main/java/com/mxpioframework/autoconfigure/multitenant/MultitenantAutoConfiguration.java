package com.mxpioframework.autoconfigure.multitenant;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration;
import com.mxpioframework.multitenant.MultitenantConfiguration;

/**
 * @author MxpIO (mailto:i@mxpio.com)
 * @since 2021年1月24日
 */
@Configuration
@AutoConfigureBefore({JpaRepositoriesAutoConfiguration.class})
@ConditionalOnClass(MultitenantConfiguration.class)
@AutoConfigureAfter({SecurityAutoConfiguration.class})
@Import({MultitenantConfiguration.class})
public class MultitenantAutoConfiguration {
	
	@Bean
	@Primary
	public MultitenantUserDetailsService MultitenantUserDetailsService() {
		return new MultitenantUserDetailsService();
	}
	
	@Bean
	public CurrentOrganizationStrategyImpl currentOrganizationStrategy() {
		return new CurrentOrganizationStrategyImpl();
	}
	
	@ConditionalOnClass(SimpleKeyGenerator.class)
	protected static class cacheConfiguration {
		
		@Bean
		public OrganizationKeyGenerator keyGenerator() {
			return new OrganizationKeyGenerator();
		}
	}
	
	
	/*@ConditionalOnClass(NoticeConfiguration.class)
	protected static class CategoryProviderConfiguration {
		
		@Bean
		@Primary
		public MultitenantCategoryProvider saasCategoryProvider() {
			return new MultitenantCategoryProvider();
		}
	}*/
	
}
