package com.mxpio.mxpioboot.security.service.impl;

import org.springframework.stereotype.Service;

import com.mxpio.mxpioboot.common.cache.CacheProvider;
import com.mxpio.mxpioboot.security.Constants;
import com.mxpio.mxpioboot.security.entity.User;
import com.mxpio.mxpioboot.security.service.OnlineUserService;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {

	@Override
	public void save(User user, String token, CacheProvider cacheProvider) {
		cacheProvider.set(Constants.JWT_TOKEN_REDIS_KEY + token, user, Constants.DEFAULT_TOKEN_TIME_MS / 1000);
	}

	@Override
	public void kickOut(String token, CacheProvider cacheProvider) {
		cacheProvider.del(Constants.JWT_TOKEN_REDIS_KEY + token);
	}

	@Override
	public void logout(String token, CacheProvider cacheProvider) {
		cacheProvider.del(Constants.JWT_TOKEN_REDIS_KEY + token);
	}

	@Override
	public User getOne(String token, CacheProvider cacheProvider) {
		return (User) cacheProvider.get(Constants.JWT_TOKEN_REDIS_KEY + token);
	}

	@Override
	public void kickOutForUsername(String username, CacheProvider cacheProvider) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkLoginOnUser(String userName, String igoreToken, CacheProvider CacheProvider) {
		// TODO Auto-generated method stub

	}

}
