package com.mxpioframework.security.access.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.mxpioframework.security.kaptcha.KaptchaAuthenticationException;
import com.mxpioframework.security.kaptcha.KaptchaProperties;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	public LoginFilter() {
		// 拦截url为 "/login" 的POST请求
		super(new AntPathRequestMatcher("/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// 从json中获取username和password
		String username = null, password = null,kaptcha = null,uuid = null;
		String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
		if (StringUtils.hasText(body)) {
			JSONObject jsonObj = JSON.parseObject(body);
			username = jsonObj.getString("username");
			password = jsonObj.getString("password");
			kaptcha = jsonObj.getString("kaptcha");
			uuid = jsonObj.getString("uuid");
		}
		KaptchaProperties kaptchaProperties = SpringUtil.getBean(KaptchaProperties.class);
		if(kaptchaProperties.getOpen()) {
			if(uuid == null || kaptcha == null) {
				throw new KaptchaAuthenticationException("验证码错误");
			}
			CacheProvider cacheProvider = SpringUtil.getBean(CacheProvider.class);
			if(!kaptcha.equals(cacheProvider.get(Constants.KAPTCHA_REDIS_KEY+uuid)+"")) {
				throw new KaptchaAuthenticationException("验证码错误");
			}
		}

		if (username == null)
			username = "";
		if (password == null)
			password = "";
		username = username.trim();
		// 封装到token中提交
		JwtLoginToken authRequest = new JwtLoginToken(username, password);

		return this.getAuthenticationManager().authenticate(authRequest);
	}
}