package com.mxpioframework.security;

import java.io.IOException;

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
import com.mxpioframework.security.anthentication.JwtAuthenticationProvider;
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
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CacheProvider cacheProvider;
	
	@Autowired
	private ObjectMapper objectMapper;
	
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
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter();
        
        FilterSecurityInterceptor securityInterceptor = createFilterSecurityInterceptor();
        
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(userDetailsService, passwordEncoder);
        
        http.authenticationProvider(jwtAuthenticationProvider).cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // ?????????????????????????????????????????????
				.antMatchers(mergeAnonymous()).permitAll()
				// ??????SWAGGER??????
				.antMatchers(Constants.SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated()  // ??????????????????????????????
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new MxpioAccessDeniedHandler())
                .and()
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtTokenFilter, LoginFilter.class)
                .addFilterAfter(securityInterceptor,
        				org.springframework.security.web.access.intercept.FilterSecurityInterceptor.class)
                .logout() // ?????????????????????logout???????????????????????????????????????
                .logoutUrl(URL_PREFIX + logoutPath)
                .logoutSuccessUrl("/login")// ??????????????????????????????????????????????????????????????????;
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

	//??????????????????
	class JwtLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
	        response.setContentType("application/json;charset=UTF-8");
	        
	        User jwtUserDetails = (User) authentication.getPrincipal();
	        String accessToken = TokenUtil.createAccessToken(jwtUserDetails);
	        String refreshToken = TokenUtil.createRefreshToken(accessToken);
	        //??????token
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
            response.getWriter().write(objectMapper.writeValueAsString(Result.OK("????????????",null)));
		}
	}

	class MxpioAuthenticationEntryPoint implements AuthenticationEntryPoint {

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
				throws IOException, ServletException {
			response.setContentType("application/json;charset=UTF-8");
			if(exception instanceof CaptchaAuthenticationException) {
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth(exception.getMessage())));
			}else if(exception instanceof BadCredentialsException){
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth("?????????????????????")));
			}else if(exception instanceof AccountStatusException){
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth("???????????????")));
			}else if(exception instanceof InsufficientAuthenticationException){
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth("Token??????")));
			}else if(exception instanceof NonceExpiredException){
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth("Nonce?????????")));
			}else if(exception instanceof UsernameNotFoundException){
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth("???????????????")));
			}else {
				response.getWriter().write(objectMapper.writeValueAsString(Result.noauth("????????????")));
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
	        result.setMessage("??????????????????????????????");
	        response.getWriter().write(JSON.toJSONString(result));
		}

	}
}
