package com.mxpioframework.security.anthentication;

import com.mxpioframework.security.entity.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 三方平台登录鉴权校验具体实现
 */
@Component("thirdAuthorizeProvider")
public class ThirdAuthorizeProvider implements AuthenticationProvider {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired(required = false)
	private Collection<ThirdAuthorizeUserProvider> providers;

	/**
	 * 鉴权具体逻辑
	 *
	 * @param authentication 身份凭证
	 * @return 身份凭证（已认证）
	 * @throws AuthenticationException 认证异常
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		ThirdAuthorizeToken thirdAuthorizeToken = (ThirdAuthorizeToken) authentication;
		if (CollectionUtils.isEmpty(providers)){
			throw new ThirdAuthorizeException("未匹配到对应的三方平台获取用户的实现");
		}
		for(ThirdAuthorizeUserProvider provider : providers){
			if(provider.support(thirdAuthorizeToken)){
				User user = provider.getUser((String) thirdAuthorizeToken.getCredentials());
				if (user==null){
					throw new ThirdAuthorizeException("根据三方ID获取用户信息失败！请检查用户信息");
				}
				UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
				// 构造已认证的authentication
				return new ThirdAuthorizeToken(userDetails, thirdAuthorizeToken.getCredentials(),
						userDetails.getAuthorities());
			}
		}
		throw new ThirdAuthorizeException("未匹配到对应的三方平台获取用户的实现");
	}

	/**
	 * 是否支持当前类
	 *
	 * @param authentication 身份凭证
	 * @return 是否支持
	 */
	public boolean supports(Class<?> authentication) {
		return (ThirdAuthorizeToken.class.isAssignableFrom(authentication));
	}
}