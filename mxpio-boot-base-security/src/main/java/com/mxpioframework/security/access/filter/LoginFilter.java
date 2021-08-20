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
import com.mxpioframework.security.anthentication.JwtLoginToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	public LoginFilter() {
		// 拦截url为 "/login" 的POST请求
		super(new AntPathRequestMatcher("/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// 从json中获取username和password
		String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
		String username = null, password = null;
		if (StringUtils.hasText(body)) {
			try {
				JSONObject jsonObj = JSON.parseObject(body);
				username = jsonObj.getString("username");
				password = jsonObj.getString("password");
			} catch (Exception e) {
				log.error(e.getMessage());
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