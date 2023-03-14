package com.mxpioframework.security;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mxpioframework.security.access.policy.PasswordCheckPolicy;
import com.mxpioframework.security.anthentication.JwtAuthenticationProvider;

@Configuration
@AutoConfigurationPackage
@ComponentScan
public class SecurityConfiguration {

	@Bean
	public PasswordEncoder standardPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	@ConditionalOnMissingBean(AuthenticationProvider.class)
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, List<PasswordCheckPolicy> passwordCheckPolicies) {
		return new JwtAuthenticationProvider(userDetailsService, passwordEncoder, passwordCheckPolicies);
	}
}
