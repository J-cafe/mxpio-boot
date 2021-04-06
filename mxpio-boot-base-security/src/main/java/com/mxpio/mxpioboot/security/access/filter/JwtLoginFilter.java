package com.mxpio.mxpioboot.security.access.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.mxpio.mxpioboot.security.anthentication.JwtLoginToken;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = obtainUsername(request);
		username = (username != null) ? username : "";
		username = username.trim();
		String password = obtainPassword(request);
		password = (password != null) ? password : "";
		//创建未认证的凭证(etAuthenticated(false)),注意此时凭证中的主体principal为用户名
        JwtLoginToken jwtLoginToken = new JwtLoginToken(username, password);
        //将认证详情(ip,sessionId)写到凭证
        jwtLoginToken.setDetails(new WebAuthenticationDetails(request));
        
        //AuthenticationManager获取受支持的AuthenticationProvider(这里也就是JwtAuthenticationProvider),
        //生成已认证的凭证,此时凭证中的主体为userDetails  --- 这里会委托给AuthenticationProvider实现类来验证
        // 即 跳转到 JwtAuthenticationProvider.authenticate 方法中认证
        Authentication authenticatedToken = this.getAuthenticationManager().authenticate(jwtLoginToken);
        return authenticatedToken;
		
		
		/*UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);*/
	}
}
