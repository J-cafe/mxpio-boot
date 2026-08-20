package com.mxpioframework.oauth.server.config;

import com.mxpioframework.oauth.server.token.MxpioOAuth2TokenGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

/**
 * OAuth2 授权服务器配置（适配 Spring Authorization Server 7.0 / Spring Boot 4）。
 *
 * <p>由于 mxpio-security 已定义自身的 {@link SecurityFilterChain}，Spring Boot 关于授权服务器的
 * 默认安全链（{@code @ConditionalOnMissingBean(SecurityFilterChain.class)}）不会生效，故在此显式提供
 * 授权服务器安全链。该链以最高优先级匹配授权服务器端点（/oauth2/**）及登录页（/oauth2/login），
 * 不影响 mxpio-security 的原有登录逻辑。
 *
 * <p>JWKSource、JwtDecoder、AuthorizationServerSettings 由 Spring Boot 自动配置提供，无需在此声明。
 * RegisteredClientRepository 由 {@link com.mxpioframework.oauth.server.client.JpaRegisteredClientRepository}
 * 提供（基于数据库表 MB_OAUTH_CLIENT_DETAILS）。
 */
@Configuration
public class OAuth2ServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			MxpioOAuth2TokenGenerator tokenGenerator, UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServer = new OAuth2AuthorizationServerConfigurer();

		// 匹配授权服务器端点 + 登录页（登录页需与本链同链，表单登录过滤器才能处理 POST /oauth2/login）
		RequestMatcher endpointsMatcher = authorizationServer.getEndpointsMatcher();
		RequestMatcher loginMatcher = PathPatternRequestMatcher.pathPattern("/oauth2/login");
		http.securityMatcher(new OrRequestMatcher(endpointsMatcher, loginMatcher));

		// 应用授权服务器配置，并接入自定义令牌生成器（桥接 mxpio 令牌）
		http.with(authorizationServer, oauth2 -> oauth2.tokenGenerator(tokenGenerator));

		// 表单登录：复用 mxpio 的 UserDetailsService + PasswordEncoder。
		// mxpio 原有的 AuthenticationManager 仅支持 JwtLoginToken，无法处理表单登录的 UsernamePasswordAuthenticationToken，
		// 故此处补充 DaoAuthenticationProvider。
		// 注意：必须用 authenticationProvider(...) 注册，而不是 authenticationManager(new ProviderManager(...)) 覆盖。
		// Spring Authorization Server 7.0 的 OAuth2ClientAuthenticationFilter / OAuth2TokenEndpointFilter 等均通过
		// http.getSharedObject(AuthenticationManager.class) 取用共享 AuthenticationManager；若用 authenticationManager(...)
		// 显式覆盖，HttpSecurity.beforeConfigure() 便不再从 registry 构建，ClientSecretAuthenticationProvider 及各 token
		// grant provider 会全部丢失，导致 /oauth2/token 抛 ProviderNotFoundException。
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		http.authenticationProvider(daoAuthenticationProvider);

		http.formLogin(form -> form
				.loginPage("/oauth2/login")
				.loginProcessingUrl("/oauth2/login")
				.permitAll());

		// 授权码流程需要会话保存资源所有者的登录状态（mxpio-security 为无状态，仅作用于其自身链，互不影响）
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/oauth2/login").permitAll()
				.anyRequest().authenticated());

		// 未认证的浏览器（HTML）请求跳转到登录页；令牌端点的 CSRF 已由 OAuth2AuthorizationServerConfigurer 忽略
		MediaTypeRequestMatcher htmlMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
		htmlMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
		http.exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(
				new LoginUrlAuthenticationEntryPoint("/oauth2/login"), htmlMatcher));

		return http.build();
	}

}
