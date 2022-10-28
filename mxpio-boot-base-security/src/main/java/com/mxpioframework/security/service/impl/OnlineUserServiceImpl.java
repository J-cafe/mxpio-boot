package com.mxpioframework.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.util.TokenUtil;
import com.mxpioframework.security.vo.TokenVo;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void save(User user, String token, String refreshToken, CacheProvider cacheProvider) {
		cacheProvider.set(Constants.JWT_ACCESS_TOKEN_REDIS_KEY + token, user, Constants.DEFAULT_ACCESS_TOKEN_TIME_MS / 1000L);
		cacheProvider.set(Constants.JWT_REFRESH_TOKEN_REDIS_KEY + refreshToken, token, Constants.DEFAULT_REFRESH_TOKEN_TIME_MS / 1000L);
	}
	
	@Override
	public void refresh(User user, String token,  CacheProvider cacheProvider) {
		cacheProvider.set(Constants.JWT_ACCESS_TOKEN_REDIS_KEY + token, user, Constants.DEFAULT_ACCESS_TOKEN_TIME_MS / 1000L);
	}

	@Override
	public void kickOut(String token, CacheProvider cacheProvider) {
		cacheProvider.del(Constants.JWT_ACCESS_TOKEN_REDIS_KEY + token);
	}

	@Override
	public void logout(String token, CacheProvider cacheProvider) {
		cacheProvider.del(Constants.JWT_ACCESS_TOKEN_REDIS_KEY + token);
	}

	@Override
	public User getOne(String token, CacheProvider cacheProvider) {
		return (User) cacheProvider.get(Constants.JWT_ACCESS_TOKEN_REDIS_KEY + token);
	}

	@Override
	public void kickOutForUsername(String username, CacheProvider cacheProvider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkLoginOnUser(String userName, String igoreToken, CacheProvider CacheProvider) {
		// TODO Auto-generated method stub

	}

	@Override
	public TokenVo refreshToken(String refreshToken, CacheProvider cacheProvider) {
		String token = (String) cacheProvider.get(Constants.JWT_REFRESH_TOKEN_REDIS_KEY + refreshToken);
		if(token != null) {
			TokenVo tokenVo = new TokenVo();
			String username = TokenUtil.getUser(token).getUsername();
			User user = (User) userDetailsService.loadUserByUsername(username);
			String newToken = TokenUtil.createAccessToken(user);
			String newRefreshToken = TokenUtil.createRefreshToken(newToken);
			save(user, newToken, newRefreshToken, cacheProvider);
			tokenVo.setRefreshToken(newRefreshToken);
			tokenVo.setToken(newToken);
			tokenVo.setUser(user);
			return tokenVo;
		}else {
			return null;
		}
		
	}

}
