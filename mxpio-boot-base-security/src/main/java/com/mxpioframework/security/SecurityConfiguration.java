package com.mxpioframework.security;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AutoConfigurationPackage
@ComponentScan
public class SecurityConfiguration {

	@Bean
	public PasswordEncoder standardPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	/*@Bean(name="jwtAuthenticationProvider")
	@ConditionalOnMissingBean(AuthenticationProvider.class)
	public AuthenticationProvider jwtAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, List<PasswordCheckPolicy> passwordCheckPolicies) {
		return new JwtAuthenticationProvider(userDetailsService, passwordEncoder, passwordCheckPolicies);
	}*/
}
