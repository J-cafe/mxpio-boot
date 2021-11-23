package com.mxpioframework.security.service;

import org.springframework.scheduling.annotation.Async;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.vo.TokenVo;

public interface OnlineUserService {
	
	/**
     * 保存在线用户信息
     * @param jwtUserDto /
     * @param token /
     * @param request /
     */
	public void save(User user, String token, String refreshToken, CacheProvider cacheProvider);
	
	/**
     * 踢出用户
     * @param key /
     */
	public void kickOut(String key, CacheProvider cacheProvider);
	
	/**
     * 退出登录
     * @param token /
     */
    public void logout(String token, CacheProvider cacheProvider);
    
    /**
     * 查询用户
     * @param key /
     * @return /
     */
    public User getOne(String key, CacheProvider cacheProvider);
    
    /**
     * 根据用户名强退用户
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username, CacheProvider cacheProvider) throws Exception;
    
    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken, CacheProvider cacheProvider);
    
    /**
     * 刷新token
     * @param token
     */
    public TokenVo refreshToken(String refreshToken, CacheProvider cacheProvider);

}
