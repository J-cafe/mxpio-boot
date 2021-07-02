package com.mxpio.mxpioboot.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mxpio.mxpioboot.common.cache.CacheProvider;
import com.mxpio.mxpioboot.common.util.SpringUtil;
import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.security.access.filter.JwtTokenFilter;
import com.mxpio.mxpioboot.security.access.filter.LoginFilter;
import com.mxpio.mxpioboot.security.access.intercept.FilterSecurityInterceptor;
import com.mxpio.mxpioboot.security.anthentication.JwtAuthenticationProvider;
import com.mxpio.mxpioboot.security.entity.User;
import com.mxpio.mxpioboot.security.service.OnlineUserService;
import com.mxpio.mxpioboot.security.vo.TokenVo;

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
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LoginFilter jwtLoginFilter = new LoginFilter();
		jwtLoginFilter.setAuthenticationManager(authenticationManagerBean());
        jwtLoginFilter.setAuthenticationSuccessHandler(new JwtLoginSuccessHandler());
        jwtLoginFilter.setAuthenticationFailureHandler(new HttpStatusLoginFailureHandler());
        
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter();
        
        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(userDetailsService, passwordEncoder);
        
		http.authenticationProvider(jwtAuthenticationProvider)
			// 请求验证规则
			.authorizeRequests()
				// 添加系统及客户定义的白名单地址
				.antMatchers(mergeAnonymous()).permitAll()
				// 添加SWAGGER地址
				.antMatchers(Constants.SWAGGER_WHITELIST).permitAll()
				// 剩余请求全部验证
				.anyRequest().authenticated()
				.and()
			.logout()
				.logoutUrl(URL_PREFIX + logoutPath)
				.logoutSuccessHandler(new JwtLogoutSuccessHandler())
				.permitAll()
				.and()
			.rememberMe();

		http.sessionManagement().disable()  //禁用session
	    	.formLogin().disable() //禁用form登录
	    	.csrf().disable()
	    	.addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class) // 添加拦截器
            .addFilterAfter(jwtTokenFilter, LoginFilter.class);;
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
	        String json = JSON.toJSONString(jwtUserDetails);
	        long nowMillis = System.currentTimeMillis();
	        Date now = new Date(nowMillis);
	        long expMillis = nowMillis + Constants.DEFAULT_TOKEN_TIME_MS;
            Date exp = new Date(expMillis);
            Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_TOKEN_SALT);
	        String token = JWT.create()
	    		.withSubject(json)
	            .withExpiresAt(exp)
	            .withIssuedAt(now)
	            .sign(algorithm);
	        //签发token
            if(cacheProvider != null) {
            	OnlineUserService onlineUserService = SpringUtil.getBean(OnlineUserService.class);
            	onlineUserService.save(jwtUserDetails, token, cacheProvider);
            }
	        TokenVo tokenVo = new TokenVo();
	        tokenVo.setUser(jwtUserDetails);
	        tokenVo.setToken(token);
	        response.getWriter().write(JSON.toJSONString(Result.OK(tokenVo)));
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
		}
	}

	//登录失败处理
	class HttpStatusLoginFailureHandler implements AuthenticationFailureHandler {
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSON.toJSONString(Result.noauth("账号或密码错误")));
			// response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}
}
