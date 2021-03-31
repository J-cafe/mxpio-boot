package com.mxpio.mxpioboot.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mxpio.mxpioboot.security.access.intercept.FilterSecurityInterceptor;

@Component
@Order(120)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	private static final String URL_PREFIX = "/";

	@Value("${mxpio.loginPath}")
	private String loginPath;

	@Value("${mxpio.logoutPath}")
	private String logoutPath;

	@Value("${mxpio.systemAnonymous}")
	private String systemAnonymous;

	@Value("${mxpio.customAnonymous}")
	private String customAnonymous;

	@Autowired
	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	@Autowired
	private AccessDecisionManager accessDecisionManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(mergeAnonymous())
				.permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.successHandler(new CustomAuthenticationSuccessHandler())
				.loginPage(URL_PREFIX + loginPath)
				.permitAll()
				.and()
			.logout()
				.logoutUrl(URL_PREFIX + logoutPath)
				.permitAll()
				.and()
			.rememberMe();

		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.headers().xssProtection().disable();
		http.headers().disable();

		FilterSecurityInterceptor securityInterceptor = createFilterSecurityInterceptor();
		http.addFilterAfter(securityInterceptor,
				org.springframework.security.web.access.intercept.FilterSecurityInterceptor.class);
		http.setSharedObject(FilterSecurityInterceptor.class, securityInterceptor);
	}

	private FilterSecurityInterceptor createFilterSecurityInterceptor() throws Exception {
		FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
		securityInterceptor.setSecurityMetadataSource(securityMetadataSource);
		securityInterceptor.setAccessDecisionManager(accessDecisionManager);
		securityInterceptor.setAuthenticationManager(this.authenticationManagerBean());
		securityInterceptor.afterPropertiesSet();
		return securityInterceptor;
	}

	private String[] mergeAnonymous() {
		String[] anonymous = null;
		if (StringUtils.hasText(systemAnonymous) && StringUtils.hasText(customAnonymous)) {
			anonymous = (systemAnonymous + "," + customAnonymous + ",/v2/api-docs,/doc.html,/swagger-ui.html").split(",");
		} else if (StringUtils.hasText(systemAnonymous)) {
			anonymous = (systemAnonymous + ",/v2/api-docs,/doc.html,/swagger-ui.html").split(",");
		} else if (StringUtils.hasText(customAnonymous)) {
			anonymous = (customAnonymous + ",/v2/api-docs,/doc.html,/swagger-ui.html").split(",");
		}
		return anonymous;
	}

	class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

		private RequestCache requestCache = new HttpSessionRequestCache();

		public CustomAuthenticationSuccessHandler() {
			this.setRequestCache(requestCache);
		}

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws ServletException, IOException {
			SavedRequest savedRequest = requestCache.getRequest(request, response);
			if (savedRequest != null) {
				this.getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
			} else {
				super.onAuthenticationSuccess(request, response, authentication);
			}
		}
	}
}
