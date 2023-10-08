package com.mxpioframework.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.CommonConstant;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.access.filter.JwtTokenFilter;
import com.mxpioframework.security.access.filter.LoginFilter;
import com.mxpioframework.security.access.intercept.FilterSecurityInterceptor;
import com.mxpioframework.security.captcha.CaptchaAuthenticationException;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.util.TokenUtil;
import com.mxpioframework.security.vo.TokenVo;

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
	
	/*@Autowired
	private FilterSecurityInterceptor securityInterceptor;*/

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CacheProvider cacheProvider;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Resource(name = "jwtAuthenticationProvider")
	private AuthenticationProvider jwtAuthenticationProvider;

	@Resource(name = "thirdAuthorizeProvider")
	private AuthenticationProvider thirdAuthorizeProvider;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LoginFilter jwtLoginFilter = new LoginFilter();
		jwtLoginFilter.setAuthenticationManager(authenticationManagerBean());
        jwtLoginFilter.setAuthenticationSuccessHandler(new JwtLoginSuccessHandler());
        // jwtLoginFilter.setAuthenticationFailureHandler(new HttpStatusLoginFailureHandler());
		jwtLoginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler(){
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
				//exception.printStackTrace();
				response.setContentType("application/json;charset=UTF-8");
				if(exception instanceof UsernameNotFoundException){
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth40101(exception.getMessage())));
				}else if(exception instanceof BadCredentialsException){
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth40101("密码错误")));
				}else if(exception instanceof CaptchaAuthenticationException) {
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth40101(exception.getMessage())));
				}else if(exception instanceof AccountStatusException){
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth40101("账号已锁定")));
				}else if(exception instanceof InsufficientAuthenticationException){
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth40101("Token异常")));
				}else if(exception instanceof NonceExpiredException){
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth40101("Nonce已过期")));
				}else if(exception instanceof DataAuthenticationException){
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth403("无权访问")));
				}else {
					response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401("登录异常")));
				}
			}
		});
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter();
        
        FilterSecurityInterceptor securityInterceptor = createFilterSecurityInterceptor();
        
//        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(userDetailsService, passwordEncoder);
        
        http.authenticationProvider(jwtAuthenticationProvider).authenticationProvider(thirdAuthorizeProvider).cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 添加系统及客户定义的白名单地址
				.antMatchers(mergeAnonymous()).permitAll()
				// 添加SWAGGER地址
				.antMatchers(Constants.SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated()  // 所有请求需要身份认证
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new MxpioAccessDeniedHandler())
                .and()
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtTokenFilter, LoginFilter.class)
                .addFilterBefore(securityInterceptor,
        				org.springframework.security.web.access.intercept.FilterSecurityInterceptor.class)
                .logout() // 默认注销行为为logout，可以通过下面的方式来修改
                .logoutUrl(URL_PREFIX + logoutPath)
                .logoutSuccessUrl("/login")// 设置注销成功后跳转页面，默认是跳转到登录页面;
                .logoutSuccessHandler(new JwtLogoutSuccessHandler())
                .permitAll()
                .and()
    			.rememberMe();
        
        http.setSharedObject(FilterSecurityInterceptor.class, securityInterceptor);
        http.exceptionHandling().authenticationEntryPoint(new MxpioAuthenticationEntryPoint()).accessDeniedHandler(new MxpioAccessDeniedHandler());
        
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
		if (StringUtils.isNotBlank(systemAnonymous) && StringUtils.isNotBlank(customAnonymous)) {
			anonymous = (systemAnonymous + "," + customAnonymous).split(",");
		} else if (StringUtils.isNotBlank(systemAnonymous)) {
			anonymous = (systemAnonymous).split(",");
		} else if (StringUtils.isNotBlank(customAnonymous)) {
			anonymous = (customAnonymous).split(",");
		}
		return anonymous;
	}

	//登录成功处理
	class JwtLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
	        response.setContentType("application/json;charset=UTF-8");
	        
	        User jwtUserDetails = (User) authentication.getPrincipal();
	        String accessToken = TokenUtil.createAccessToken(jwtUserDetails);
	        String refreshToken = TokenUtil.createRefreshToken(accessToken);
	        //签发token
            if(cacheProvider != null) {
            	OnlineUserService onlineUserService = SpringUtil.getBean(OnlineUserService.class);
            	onlineUserService.save(jwtUserDetails, accessToken, refreshToken, cacheProvider);
            }
	        TokenVo tokenVo = new TokenVo();
	        tokenVo.setUser(jwtUserDetails);
	        tokenVo.setToken(accessToken);
	        tokenVo.setRefreshToken(refreshToken);
	        response.getWriter().write(objectMapper.writeValueAsString(Result.OK(tokenVo)));
	    }
	}
	
	class JwtLogoutSuccessHandler implements LogoutSuccessHandler{

		@Override
		public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			
			String authInfo = request.getHeader("Authorization");
			String token = StringUtils.removeStart(authInfo, "Bearer ");
            if(cacheProvider != null) {
            	OnlineUserService onlineUserService = SpringUtil.getBean(OnlineUserService.class);
            	onlineUserService.logout(token, cacheProvider);
            }
            response.getWriter().write(objectMapper.writeValueAsString(Result.OK("注销成功",null)));
		}
	}

	class MxpioAuthenticationEntryPoint implements AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
				throws IOException, ServletException {
			response.setContentType("application/json;charset=UTF-8");
			//exception.printStackTrace();
			if(exception instanceof CaptchaAuthenticationException) {
				response.setStatus(401);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401(exception.getMessage())));
			}else if(exception instanceof BadCredentialsException){
				response.setStatus(401);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401("账号或密码错误")));
			}else if(exception instanceof AccountStatusException){
				response.setStatus(401);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401("账号已锁定")));
			}else if(exception instanceof InsufficientAuthenticationException){
				response.setStatus(401);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401("Token异常")));
			}else if(exception instanceof NonceExpiredException){
				response.setStatus(401);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401("Nonce已过期")));
			}else if(exception instanceof UsernameNotFoundException){
				response.setStatus(401);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401("用户未找到")));
			}else if(exception instanceof DataAuthenticationException){
				response.setStatus(403);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth403("无权访问")));
			}else {
				response.setStatus(401);
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth401("登录异常")));
			}
		}
		
	}
	
	class MxpioAccessDeniedHandler implements AccessDeniedHandler {

		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2)
				throws IOException, ServletException {
			response.setContentType("application/json;charset=UTF-8");
	        Result<String> result = new Result<>();
	        result.setSuccess(false);
	        result.setCode(CommonConstant.HTTP_NO_AUTHZ_403);
	        result.setMessage("无权访问");
	        response.setStatus(403);
	        response.getWriter().write(JSON.toJSONString(result));
		}

	}
}
