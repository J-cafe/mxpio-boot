package com.mxpioframework.security.access.filter;

import java.io.IOException;

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

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.security.anthentication.JwtLoginToken;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Token有效性验证拦截器
 */
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

	private RequestMatcher requiresAuthenticationRequestMatcher;
    private RequestMatcher websocketAuthenticationRequestMatcher;

	public JwtTokenFilter() {
		this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Access-Token");
        this.websocketAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Sec-WebSocket-Protocol");
	}

	protected String getJwtToken(HttpServletRequest request,HttpServletResponse httpServletResponse) {
		String authInfo = request.getHeader("Access-Token");
        if(StringUtils.isNotBlank(authInfo)){
            return authInfo;
        }
        //尝试websocket
        authInfo = request.getHeader("Sec-WebSocket-Protocol");
        if(StringUtils.isNotBlank(authInfo)){
            httpServletResponse.setHeader("Sec-WebSocket-Protocol",authInfo);
        }
		return authInfo;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!requiresAuthentication(httpServletRequest, httpServletResponse)) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
        try {
        	String token = getJwtToken(httpServletRequest,httpServletResponse);
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
                } else {
            		JwtLoginToken jwtLoginToken = new JwtLoginToken(user, "", user.getAuthorities());
                    jwtLoginToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(jwtLoginToken);
                    // onlineUserService.refresh(user, token, cacheProvider);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
            	}
            }else {
            	/*DecodedJWT jwt = TokenUtil.verifyToken(token);
                Date date = jwt.getExpiresAt();

                if(date.before(new Date())) {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
                User user = JSON.parseObject(jwt.getSubject(), User.class);
                JwtLoginToken jwtLoginToken = new JwtLoginToken(user, "", user.getAuthorities());
                jwtLoginToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(jwtLoginToken);
                filterChain.doFilter(httpServletRequest, httpServletResponse);*/
                throw new Exception("缓存服务未配置");
            }
        } catch (Exception e) {
        	//log.info(httpServletRequest.getRequestURI());
            log.error("错误信息：{}", e.getMessage());
            e.fillInStackTrace();
        }
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request)||websocketAuthenticationRequestMatcher.matches(request);
	}
}
