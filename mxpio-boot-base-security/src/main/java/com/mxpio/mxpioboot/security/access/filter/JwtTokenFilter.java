package com.mxpio.mxpioboot.security.access.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mxpio.mxpioboot.common.CommonConstant;
import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.security.anthentication.JwtLoginToken;
import com.mxpio.mxpioboot.security.entity.User;

/**
 * @Data: 2019/10/30
 * @Des: Token有效性验证拦截器
 */
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private RequestMatcher requiresAuthenticationRequestMatcher;
	
	public JwtTokenFilter() {
		this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Authorization");
	}
	
	protected String getJwtToken(HttpServletRequest request) {
		String authInfo = request.getHeader("Authorization");
		return StringUtils.removeStart(authInfo, "Bearer ");
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
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                Result<String> result = new Result<>();
                result.setSuccess(false);
                result.setCode(CommonConstant.HTTP_NO_AUTHZ);
                result.setMessage("未登录");
                httpServletResponse.getWriter().write(JSON.toJSONString(result));
                return;
            }
            
            String salt = "mxpio";
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier v = JWT.require(algorithm).build();
            DecodedJWT jwt = v.verify(token);
            
            Date date = jwt.getExpiresAt();
            if(date.before(new Date())) {
            	httpServletResponse.setContentType("application/json;charset=UTF-8");
                Result<String> result = new Result<>();
                result.setSuccess(false);
                result.setCode(CommonConstant.HTTP_NO_AUTHZ);
                result.setMessage("登陆失效，请重新登陆");
                httpServletResponse.getWriter().write(JSON.toJSONString(result));
                return;
            }

            User user = JSON.parseObject(jwt.getClaim("userDetails").asString(), User.class);
            JwtLoginToken jwtLoginToken = new JwtLoginToken(user, "", user.getAuthorities());
            jwtLoginToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(jwtLoginToken);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("登陆凭证失效，请重新登陆");
        }
    }
    
    protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request);
	}
}
