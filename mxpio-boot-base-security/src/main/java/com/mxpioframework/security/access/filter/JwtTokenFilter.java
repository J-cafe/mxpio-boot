package com.mxpioframework.security.access.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.security.anthentication.JwtLoginToken;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Token有效性验证拦截器
 */
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private RequestMatcher requiresAuthenticationRequestMatcher;
	
	public JwtTokenFilter() {
		this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Access-Token");
	}
	
	protected String getJwtToken(HttpServletRequest request) {
		String authInfo = request.getHeader("Access-Token");
		return authInfo;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    	
    	if (!requiresAuthentication(httpServletRequest, httpServletResponse)) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
        try {
        	String token = getJwtToken(httpServletRequest);
            if (StringUtils.isEmpty(token)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            CacheProvider cacheProvider = SpringUtil.getBean(CacheProvider.class);
            OnlineUserService onlineUserService = SpringUtil.getBean(OnlineUserService.class);
            if(cacheProvider != null) {
            	User user = onlineUserService.getOne(token, cacheProvider);
            	if(user == null) {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
            	} else {
            		JwtLoginToken jwtLoginToken = new JwtLoginToken(user, "", user.getAuthorities());
                    jwtLoginToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(jwtLoginToken);
                    onlineUserService.refresh(user, token, cacheProvider);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
            	}
            }else {
            	DecodedJWT jwt = TokenUtil.verifyToken(token);
                Date date = jwt.getExpiresAt();
                
                if(date.before(new Date())) {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
                User user = JSON.parseObject(jwt.getSubject(), User.class);
                JwtLoginToken jwtLoginToken = new JwtLoginToken(user, "", user.getAuthorities());
                jwtLoginToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(jwtLoginToken);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } catch (Exception e) {
        	log.info(httpServletRequest.getRequestURI());
        	log.error("错误信息：" + e.getMessage());
        	e.printStackTrace();
        }
    }
    
    protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request);
	}
}
