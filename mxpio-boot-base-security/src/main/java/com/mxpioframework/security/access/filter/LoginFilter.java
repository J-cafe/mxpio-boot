package com.mxpioframework.security.access.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mxpioframework.security.anthentication.ThirdAuthorizeToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.anthentication.JwtLoginToken;
import com.mxpioframework.security.captcha.CaptchaAuthenticationException;
import com.mxpioframework.security.captcha.CaptchaProperties;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	public LoginFilter() {
		// 拦截url为 "/login" 的POST请求
		super(new AntPathRequestMatcher("/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// 从json中获取username和password
		String username = null, password = null,captcha = null,uuid = null,authCode = null,thirdPlatformType = null;
		authCode = request.getParameter("authCode");
		thirdPlatformType = request.getParameter("thirdPlatformType");
		username = request.getParameter("username");
		password = request.getParameter("password");
		captcha = request.getParameter("captcha");
		uuid = request.getParameter("uuid");
		String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
		if (StringUtils.hasText(body) && username == null) {
			JSONObject jsonObj = JSON.parseObject(body);
			username = jsonObj.getString("username");
			password = jsonObj.getString("password");
			captcha = jsonObj.getString("captcha");
			uuid = jsonObj.getString("uuid");
			authCode = jsonObj.getString("authCode");
			thirdPlatformType = jsonObj.getString("thirdPlatformType");
		}

		if(authCode!=null&&!authCode.equals("")){//三方登录
			// 封装到token中提交
			ThirdAuthorizeToken authRequest = new ThirdAuthorizeToken(authCode,authCode, thirdPlatformType);
			return this.getAuthenticationManager().authenticate(authRequest);
		}else{
			//常规校验
			CaptchaProperties captchaProperties = SpringUtil.getBean(CaptchaProperties.class);
			if(captchaProperties.getOpen()) {
				if(uuid == null || captcha == null) {
					throw new CaptchaAuthenticationException("验证码错误");
				}
				CacheProvider cacheProvider = SpringUtil.getBean(CacheProvider.class);
				if(!captcha.equals(cacheProvider.get(Constants.CAPTCHA_REDIS_KEY+uuid)+"")) {
					throw new CaptchaAuthenticationException("验证码错误");
				}
			}
			if(username == null || password == null) {
				throw new BadCredentialsException("账户名密码错误！");
			}
			// 封装到token中提交
			JwtLoginToken authRequest = new JwtLoginToken(username, password);
			return this.getAuthenticationManager().authenticate(authRequest);
		}
	}
}